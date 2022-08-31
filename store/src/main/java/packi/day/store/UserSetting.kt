package packi.day.store

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager
import org.joda.time.Hours
import org.joda.time.Minutes

class UserSetting(context: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val shouldNotify: Boolean by lazy {
        preferences.getBoolean("notification", false)
    }


    // TODO
    val notificationDate: Pair<Hours, Minutes> by lazy {
        val date = preferences.getString("dateNotification", "00:00")
//        notificationHour = 0 //TimePickerPreferences.getHour(savedDate);
//        notificationMinute = 0 //TimePickerPreferences.getMinute(savedDate);

        return@lazy Pair(Hours.hours(0), Minutes.minutes(0))
    }

    //    @ColorInt
    val background: Int by lazy {
        Color.parseColor(preferences.getString("BackgroundColor", "#00000000"))
    }

    //    @ColorInt
    val widgetTextColor: Int by lazy {
        Color.parseColor(preferences.getString("TextColor", "#ff000000"))
    }

    val widgetFontSize: Float by lazy {
        preferences.getFloat("TextSize", 50f) * 30 / 100 + 10
    }


}
