package packi.day.store

import android.content.Context
import org.joda.time.MonthDay
import packi.day.store.feature.HashMapStoreDelegate

class InternationalDayRepository(context: Context, private val delegate: StoreDelegate = HashMapStoreDelegate()) {

    init {
        delegate.loadData(context)
    }

    fun get(date: MonthDay): InternationalDay {
        // First parameter should use the default value but this produce a compilation error
        return delegate.get(date)
    }

    fun count(criteria: String): Set<Int> {
        return delegate.count(criteria)
    }

    fun find(criteria: String): List<InternationalDay> {
        return delegate.find(criteria)
    }

    fun getRandom(): InternationalDay {
        return delegate.getRandom()
    }

}