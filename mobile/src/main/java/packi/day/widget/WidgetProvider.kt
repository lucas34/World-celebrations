package packi.day.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import packi.day.main.FocusCelebrationView
import packi.day.store.InternationalDayRepository
import packi.day.store.StoreLocator

class WorldCelebrationWidget : GlanceAppWidget() {

    @Composable
    override fun Content() {
        FocusCelebrationView(LocalContext.current)
    }

}

class CelebrationWidgetReceiver : GlanceAppWidgetReceiver(), StoreLocator {

    private lateinit var store: InternationalDayRepository

    override val glanceAppWidget: GlanceAppWidget = WorldCelebrationWidget()

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        store = InternationalDayRepository(context!!)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        store = InternationalDayRepository(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        store = InternationalDayRepository(context)
    }

    override fun getStore(): InternationalDayRepository {
        return store
    }

}