package packi.day.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Locale;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pm == null) return;
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean isActive = sharedPrefs.getBoolean("notification", false);

        if (isActive) {
            try {
                wl.acquire(1000);
                context.startService(new Intent(context, NotificationExecutor.class));
            } finally {
                wl.release();
            }
        }
    }

    public void init(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        String savedDate = sharedPrefs.getString("dateNotification", "00:00");
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
//        int hours = TimePickerPreferences.getHour(savedDate);
//        int minutes = TimePickerPreferences.getMinute(savedDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        calendar.set(year, month, day, 0, 0, 0);
        if (calendar.before(Calendar.getInstance(Locale.getDefault()))) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pi);
    }

    public void cancelAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        am.cancel(sender);
    }
}