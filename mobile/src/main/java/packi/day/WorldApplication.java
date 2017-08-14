package packi.day;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.journeemondialelib.WorldCelebration;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class WorldApplication extends Application {

    private static final int VERSION = 4;

    private WorldCelebration worldCelebration;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(VERSION)
                .build();
        
        Realm.setDefaultConfiguration(config);

        worldCelebration = new WorldCelebration(this);

        if (BuildConfig.DEBUG) {
            new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDeath().build();
        }
    }

    @Override
    public void onTerminate() {
        worldCelebration.close();
        super.onTerminate();
    }

    public static WorldCelebration with(Context context) {
        WorldApplication application = (WorldApplication) context.getApplicationContext();
        return application.getCelebrationHelper();
    }


    public WorldCelebration getCelebrationHelper() {
        return worldCelebration;
    }

}