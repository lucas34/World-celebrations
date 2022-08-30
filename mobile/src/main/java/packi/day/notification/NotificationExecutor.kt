package packi.day.notification

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.android.gms.analytics.GoogleAnalytics
import org.joda.time.MonthDay
import packi.day.R
import packi.day.WorldApplication
import packi.day.common.Wear
import packi.day.common.report
import packi.day.ui.ActivityMain


class NotificationExecutor : IntentService("NotificationExecutor") {

    override fun onHandleIntent(intent: Intent?) {
        val storeData = WorldApplication.with(applicationContext)
        storeData.get(TODAY).celebration?.let { celebration ->
            showNotification(celebration.name)
            Wear.sendNotification(this, celebration.name)
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
