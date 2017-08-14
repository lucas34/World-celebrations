package packi.day.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AutoStart extends BroadcastReceiver {

    private final Alarm alarm = new Alarm();

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (sharedPrefs.getBoolean("notification", false)) {
                alarm.init(context);
            } else {
                alarm.cancelAlarm(context);
            }
        }
    }
}