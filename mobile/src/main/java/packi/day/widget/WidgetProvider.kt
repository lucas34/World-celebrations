package packi.day.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

import org.joda.time.MonthDay

import packi.day.R
import packi.day.WorldApplication
import packi.day.common.UserSettings
import packi.day.ui.ActivityMain

class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val (_, name) = WorldApplication.with(context).get(MonthDay.now())

        val userSettings = UserSettings(context)

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setInt(R.id.WidgetText, "setBackgroundColor", userSettings.background)
            views.setInt(R.id.WidgetText, "setTextColor", userSettings.text)
            views.setFloat(R.id.WidgetText, "setTextSize", userSettings.size.toFloat())
            views.setTextViewText(R.id.WidgetText, name)
            views.setOnClickPendingIntent(R.id.WidgetText, getPendingIntent(context, appWidgetId))
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun getPendingIntent(context: Context, appWidgetId: Int): PendingIntent {
        val intent = Intent(context, ActivityMain::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

}