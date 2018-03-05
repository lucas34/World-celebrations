package packi.day.notification

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import org.joda.time.MonthDay
import packi.day.R
import packi.day.WorldApplication
import packi.day.common.Wear
import packi.day.ui.ActivityMain


class NotificationExecutor : IntentService("NotificationExecutor") {

    override fun onHandleIntent(intent: Intent?) {
        val storeData = WorldApplication.with(applicationContext)
        if (storeData.hasCelebration(TODAY)) {
            val (_, name) = storeData.get(TODAY)

            showNotification(name)
            Wear.sendNotification(this, name)
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
// TODO       AnalyticsTracker.getInstance(this).sendTracker("/notification/publish")
    }

    companion object {

        private val TODAY = MonthDay.now()
    }
}
