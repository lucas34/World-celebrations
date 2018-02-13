package packi.day.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.joda.time.MonthDay;

import packi.day.R;
import packi.day.WorldApplication;
import packi.day.common.UserSettings;
import packi.day.store.InternationalDay;
import packi.day.ui.ActivityMain;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        InternationalDay celebration = WorldApplication.with(context).get(MonthDay.now());

        UserSettings userSettings = new UserSettings(context);

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setInt(R.id.WidgetText, "setBackgroundColor", userSettings.background);
            views.setInt(R.id.WidgetText, "setTextColor", userSettings.text);
            views.setFloat(R.id.WidgetText, "setTextSize", userSettings.size);
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