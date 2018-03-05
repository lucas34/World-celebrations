package packi.day

import android.app.Activity
import android.os.Bundle
import packi.day.common.AnalyticsTracker
import packi.day.store.StoreData
import packi.day.store.feature.HashMapDayStore

/**
 * Created by lucasnelaupe on 5/3/18.
 */

class MainActivity: Activity() {

    lateinit var store: StoreData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        store = StoreData(applicationContext, HashMapDayStore())
    }

    override fun onResume() {
        super.onResume()
        AnalyticsTracker.getInstance(applicationContext).sendTracker("/wear/launcher")
    }

}