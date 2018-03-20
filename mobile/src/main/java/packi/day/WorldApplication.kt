package packi.day

import android.app.Application
import android.content.Context
import android.os.StrictMode
import android.provider.ContactsContract
import io.realm.Realm
import packi.day.main.DataStoreComponent
import packi.day.main.DaggerDataStoreComponent
import packi.day.main.DataStoreModule
import packi.day.store.feature.realm.RealmStoreDelegate

class WorldApplication : Application() {

    private lateinit var dataStoreComponent: DataStoreComponent

    override fun onCreate() {
        super.onCreate()
        RealmStoreDelegate.init(this)

        dataStoreComponent = DaggerDataStoreComponent.builder()
                .dataStoreModule(DataStoreModule(applicationContext, RealmStoreDelegate()))
                .build()

        if (BuildConfig.DEBUG) {
            StrictMode.ThreadPolicy.Builder().detectAll().penaltyDeath().build()
        }
    }

    companion object {
        fun getDataStoreComponent(context: Context) : DataStoreComponent {
            val worldApplication = context.applicationContext as WorldApplication
            return worldApplication.dataStoreComponent
        }
    }

    override fun onTerminate() {
        Realm.getDefaultInstance().close()
        super.onTerminate()
    }

}