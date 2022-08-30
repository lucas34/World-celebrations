package packi.day

import android.app.Application
import android.content.Context
import android.os.StrictMode
import io.realm.Realm
import io.realm.RealmConfiguration
import packi.day.store.InternationalDayRepository
import packi.day.store.StoreLocator
import packi.day.store.feature.realm.RealmStoreDelegate

class WorldApplication : Application(), StoreLocator {

    lateinit var celebrationHelper: InternationalDayRepository

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(VERSION)
                .build()

        Realm.setDefaultConfiguration(config)

        celebrationHelper = InternationalDayRepository(applicationContext, RealmStoreDelegate())

        if (BuildConfig.DEBUG) {
            StrictMode.ThreadPolicy.Builder().detectAll().penaltyDeath().build()
        }
    }

    override val store: InternationalDayRepository
        get() = celebrationHelper


    override fun onTerminate() {
        Realm.getDefaultInstance().close()
        super.onTerminate()
    }

    // TODO this cast is quite dirty
    // See how we can avoid this static method
    companion object {
        private const val VERSION = 4L

        fun with(context: Context): InternationalDayRepository {
            val application = context.applicationContext as WorldApplication
            return application.celebrationHelper
        }
    }

}