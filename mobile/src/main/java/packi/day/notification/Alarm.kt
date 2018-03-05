package packi.day.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import packi.day.store.UserSetting
import java.util.*

class Alarm : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (!UserSetting(context).shouldNotify) return

        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "")
        try {
            wl.acquire(1000)
            context.startService(Intent(context, NotificationExecutor::class.java))
        } finally {
            wl.release()
        }
    }

    fun init(context: Context) {
        val calendar = Calendar.getInstance(Locale.getDefault())

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DATE)

        val settings = UserSetting(context)

        calendar.set(year, month, day, settings.notificationDate.first.hours, settings.notificationDate.second.minutes, 0)
        if (calendar.before(Calendar.getInstance(Locale.getDefault()))) {
            calendar.add(Calendar.DATE, 1)
        }

        val intent = Intent(context, Alarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, (1000 * 60 * 60 * 24).toLong(), pendingIntent)
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, Alarm::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(sender)
    }
}


class AlarmService : Service() {

    private val alarm = Alarm()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        alarm.init(this)
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}

class AutoStart : BroadcastReceiver() {

    private val alarm = Alarm()

    override fun onReceive(context: Context, intent: Intent) {
        if ("android.intent.action.BOOT_COMPLETED" != intent.action) return

        if (UserSetting(context).shouldNotify) {
            alarm.init(context)
        } else {
            alarm.cancelAlarm(context)
        }

    }
}