package packi.day


import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.data.FreezableUtils
import com.google.android.gms.wearable.*
import packi.day.common.Constants
import java.util.concurrent.TimeUnit

class OngoingNotificationListenerService : WearableListenerService() {

    private var notificationId = 100

    private lateinit var apiClient: GoogleApiClient

    override fun onCreate() {
        super.onCreate()
        apiClient = GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build()

        apiClient.connect()
    }

    override fun onDataChanged(dataEvents: DataEventBuffer?) {
        val dataEvents = dataEvents ?: return
        val events = FreezableUtils.freezeIterable(dataEvents)
        dataEvents.release()

        if (!apiClient.isConnected) {
            val connectionResult = apiClient
                    .blockingConnect(30, TimeUnit.SECONDS)
            if (!connectionResult.isSuccess) {
                Log.e(TAG, "Service failed to connect to GoogleApiClient.")
                return
            }
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(this, NotificationActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val extender = Notification.WearableExtender()
                .setDisplayIntent(pendingIntent)

        val notificationBuilder = Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .extend(extender)

        for (event in events) {
            if (event.type != DataEvent.TYPE_CHANGED) {
                continue
            }

            val path = event.dataItem.uri.path
            if (Constants.PATH_NOTIFICATION != path) {
                Log.d(TAG, "Unrecognized path: $path")
                continue
            }

            // Get the data out of the event
            val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
            val title = dataMapItem.dataMap.getString(Constants.KEY_TITLE)

            notificationIntent.putExtra(NotificationActivity.EXTRA_TITLE, title)

            notificationManager.notify(++notificationId, notificationBuilder.build())
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent?) {
        if (messageEvent!!.path == Constants.PATH_DISMISS) {
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.cancel(notificationId)
        }
    }

    companion object {

        private val TAG = OngoingNotificationListenerService::class.java.simpleName
    }
}