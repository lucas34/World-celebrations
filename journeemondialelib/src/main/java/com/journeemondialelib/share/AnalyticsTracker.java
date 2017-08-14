package com.journeemondialelib.share;

import android.content.Context;
import android.content.pm.PackageManager;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.journeemondialelib.BuildConfig;


public final class AnalyticsTracker {

    private static AnalyticsTracker instance;

    private final Tracker tracker;

    private AnalyticsTracker(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker(BuildConfig.ANALYTICS_TOKEN);
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
        tracker.setAppVersion(getVersion(context));
    }

    private String getVersion(Context context) {
        try {
            return context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            return BuildConfig.VERSION_NAME;
        }
    }

    public static AnalyticsTracker getInstance(Context context) {
        if (instance == null) {
            instance = new AnalyticsTracker(context);
        }
        return instance;
    }

    public void sendTracker(String name) {
        tracker.setScreenName(name);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
