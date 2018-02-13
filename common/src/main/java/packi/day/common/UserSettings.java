package packi.day.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

/**
 * Created with IntelliJ
 * Created by lucas
 * Date 26/03/15
 */

public class UserSettings {

    public final int background;
    public final int text;
    public final int size;

    public UserSettings(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        background = Color.parseColor(preferences.getString("BackgroundColor", "#00000000"));
        text = Color.parseColor(preferences.getString("TextColor", "#ff000000"));
        size = preferences.getInt("TextSize", 50) * 30 / 100 + 10;
    }



}
