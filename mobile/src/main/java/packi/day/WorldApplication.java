package packi.day;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import packi.day.store.StoreData;
import packi.day.store.feature.realm.RealmDayStore;

public class WorldApplication extends Application {

    private static final int VERSION = 4;

    private StoreData worldCelebration;

    public static StoreData with(Context context) {
        WorldApplication application = (WorldApplication) context.getApplicationContext();
        return application.getCelebrationHelper();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(VERSION)
                .build();

        Realm.setDefaultConfiguration(config);

        worldCelebration = new StoreData(new RealmDayStore(this));

        if (BuildConfig.DEBUG) {
            new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDeath().build();
        }
    }

    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }

    public StoreData getCelebrationHelper() {
        return worldCelebration;
    }

}