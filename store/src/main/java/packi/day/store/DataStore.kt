package packi.day.store

import android.content.Context
import org.joda.time.MonthDay
import packi.day.store.feature.HashMapStoreDelegate
import packi.day.store.feature.R
import javax.inject.Inject

class DataStore(private val context: Context, private val delegate: StoreDelegate) {

    init {
        delegate.loadData(context)
    }

    fun get(date: MonthDay): InternationalDay {
        val celebration = delegate.get(date)

        // First parameter should use the default value but this produce a compilation error
        return celebration ?: InternationalDay(-1, date, context.getString(R.string.no_celebration))
    }

    fun hasCelebration(date: MonthDay): Boolean {
        return delegate.get(date) != null
    }

    fun find(criteria: String): List<CelebrationComponent> {
        return delegate.find(criteria)
    }

    fun getRandom(): InternationalDay {
        return delegate.getRandom()
    }

}

sealed class CelebrationComponent {
    data class Header(val date: MonthDay): CelebrationComponent()
    data class Celebration(val data: InternationalDay): CelebrationComponent()
}