package packi.day.common

import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker


private val GoogleAnalytics.newTracker : Tracker
    get() {
        this.setLocalDispatchPeriod(1800)

        val tracker = newTracker(BuildConfig.ANALYTICS_TOKEN)
        tracker.enableExceptionReporting(true)
        tracker.enableAdvertisingIdCollection(true)
        tracker.enableAutoActivityTracking(true)
        tracker.setAppVersion(BuildConfig.VERSION_NAME)
        return tracker
    }

fun GoogleAnalytics.sendTracker(name: String) {
    val tracker = this.newTracker
    tracker.setScreenName(name)
    tracker.send(HitBuilders.ScreenViewBuilder().build())
}
