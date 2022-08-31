package packi.day.ui.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.annotation.ColorInt;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import packi.day.R;
import packi.day.notification.Alarm;
import packi.day.widget.WidgetProvider;

public class PreferencesFragment extends PreferenceFragmentCompat {

    private static final String TXT_COLOR = "TextColor";
    private static final String BG_COLOR = "BackgroundColor";
    private static final String TXT_SIZE = "TextSize";
    private static final String DATE_NOTIFICATION = "dateNotification";
    private static final String NOTIFICATION = "notification";
    private static final String SET_TXT_COLOR = "setTextColor";
    private static final String SET_BG_COLOR = "setBackgroundColor";
    private static final String SET_TXT_SIZE = "setTextSize";
    private final Alarm alarm = new Alarm();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        setListener(TXT_COLOR, (pref, obj) -> update(SET_TXT_COLOR, getColor(obj)));
        setListener(BG_COLOR, (pref, obj) -> update(SET_BG_COLOR, getColor(obj)));
        setListener(TXT_SIZE, (pref, obj) -> update(SET_TXT_SIZE, getTextSize(obj)));
        setListener(DATE_NOTIFICATION, (preference, newValue) -> setAlarm());

        Preference checkBoxPreference = findPreference(NOTIFICATION);
        checkBoxPreference.setOnPreferenceChangeListener((p, check) ->
                (boolean) check ? setAlarm() : cancelAlarm());
    }

    private void setListener(CharSequence key, Preference.OnPreferenceChangeListener listener) {
        Preference preference = findPreference(key);
        if (preference != null) {
            preference.setOnPreferenceChangeListener(listener);
        }
    }

    private @ColorInt
    int getColor(Object obj) {
        return Color.parseColor(obj.toString());
    }

    private @ColorInt
    int getTextSize(Object obj) {
        return Integer.parseInt(obj.toString()) * 30 / 100 + 10;
    }

    private boolean setAlarm() {
        alarm.init(getActivity());
        return true;
    }

    private boolean cancelAlarm() {
        alarm.cancelAlarm(getActivity());
        return true;
    }

    private boolean update(String methodName, int value) {
        String packageName = getActivity().getBaseContext().getPackageName();
        RemoteViews views = new RemoteViews(packageName, R.layout.widget_layout);
        views.setInt(R.id.WidgetText, methodName, value);

        AppWidgetManager manager = AppWidgetManager.getInstance(getActivity());
        ComponentName provider = new ComponentName(getActivity().getPackageName(), WidgetProvider.class.getName());
        manager.updateAppWidget(provider, views);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
//        AnalyticsTracker.getInstance(getActivity()).sendTracker("/preferences");
//        ((ActivityMain) getActivity()).setScreenTitle(preference);
    }
}