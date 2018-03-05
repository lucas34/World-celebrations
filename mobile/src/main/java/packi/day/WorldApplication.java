package packi.day;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import packi.day.store.DataStore;
import packi.day.store.feature.realm.RealmStoreDelegate;

public class WorldApplication extends Application {

    private static final int VERSION = 4;

    private DataStore worldCelebration;

    public static DataStore with(Context context) {
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

        worldCelebration = new DataStore(getApplicationContext(), new RealmStoreDelegate());

        if (BuildConfig.DEBUG) {
            new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDeath().build();
        }
    }

    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }

    public DataStore getCelebrationHelper() {
        return worldCelebration;
    }

}