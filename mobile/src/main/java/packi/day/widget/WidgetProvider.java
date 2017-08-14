package packi.day.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.journeemondialelib.Celebration;

import org.joda.time.MonthDay;

import packi.day.R;
import packi.day.WorldApplication;
import packi.day.ui.ActivityMain;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Celebration celebration = WorldApplication.with(context).getCelebration(MonthDay.now());

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String backgroundPosition = sharedPrefs.getString("BackgroundColor", "#00000000");
        String textColor = sharedPrefs.getString("TextColor", "#ff000000");
        int textSize = sharedPrefs.getInt("TextSize", 50) * 30 / 100 + 10;

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setInt(R.id.WidgetText, "setBackgroundColor", Color.parseColor(backgroundPosition));
            views.setInt(R.id.WidgetText, "setTextColor", Color.parseColor(textColor));
            views.setFloat(R.id.WidgetText, "setTextSize", textSize);
            views.setTextViewText(R.id.WidgetText, celebration.name);
            views.setOnClickPendingIntent(R.id.WidgetText, getPendingIntent(context, appWidgetId));
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    private PendingIntent getPendingIntent(Context context, int appWidgetId) {
        Intent intent = new Intent(context, ActivityMain.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}