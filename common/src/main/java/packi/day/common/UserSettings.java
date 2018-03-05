package packi.day.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created with IntelliJ
 * Created by lucas
 * Date 26/03/15
 */

public class UserSettings {

    public final boolean shouldNotify;

    public final int notificationHour;
    public final int notificationMinute;

    public final int background;
    public final int text;
    public final int size;

    public UserSettings(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        shouldNotify = preferences.getBoolean("notification", false);

        String savedDate = preferences.getString("dateNotification", "00:00");
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        // TODO
        notificationHour = 0; //TimePickerPreferences.getHour(savedDate);
        notificationMinute = 0; //TimePickerPreferences.getMinute(savedDate);

        background = Color.parseColor(preferences.getString("BackgroundColor", "#00000000"));
        text = Color.parseColor(preferences.getString("TextColor", "#ff000000"));
        size = preferences.getInt("TextSize", 50) * 30 / 100 + 10;
    }


}
