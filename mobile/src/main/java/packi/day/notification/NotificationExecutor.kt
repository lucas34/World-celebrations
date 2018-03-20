package packi.day.notification

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.android.gms.analytics.GoogleAnalytics
import dagger.android.AndroidInjection
import org.joda.time.MonthDay
import packi.day.R
import packi.day.common.Wear
import packi.day.common.report
import packi.day.store.DataStore
import packi.day.ui.ActivityMain
import javax.inject.Inject

class NotificationExecutor : IntentService("NotificationExecutor") {

    @Inject
    lateinit var store: DataStore

    override fun onHandleIntent(intent: Intent?) {




        AndroidInjection.inject(this)

        if (store.hasCelebration(TODAY)) {
            val celebrationName = store.get(TODAY).name

            showNotification(celebrationName)
            Wear.sendNotification(this, celebrationName)
        }
    }

    private fun showNotification(celebration: String) {
        val intent = Intent(this, ActivityMain::class.java)
        val notification = NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .setContentTitle(getString(R.string.today))
                .setContentText(celebration)
                .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)

        GoogleAnalytics.getInstance(applicationContext).report("/notification/publish")
    }

    companion object {

        private val TODAY = MonthDay.now()
    }
}
