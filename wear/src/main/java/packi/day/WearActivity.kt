package packi.day

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.gms.analytics.GoogleAnalytics
import packi.day.common.report
import packi.day.image.PicassoHolder
import packi.day.main.FocusCelebrationView
import packi.day.store.InternationalDayRepository
import packi.day.store.StoreLocator

class WearActivity: StoreLocator, ComponentActivity() {

    private val actualStore by lazy  { InternationalDayRepository(this) }

    override fun getStore(): InternationalDayRepository {
        return actualStore
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PicassoHolder.appContext = this
        setContent {
            FocusCelebrationView()
        }
    }

    override fun onResume() {
        super.onResume()
        GoogleAnalytics.getInstance(applicationContext).report("/wear/launcher")
    }

}