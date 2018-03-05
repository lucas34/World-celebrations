package packi.day.store

import android.content.Context
import org.joda.time.MonthDay
import packi.day.store.feature.HashMapStoreDelegate
import packi.day.store.feature.R


class DataStore(private val context: Context, private val delegate: StoreDelegate = HashMapStoreDelegate()) {

    init {
        delegate.loadData(context)
    }

    fun get(date: MonthDay) : InternationalDay? {
        val celebration = delegate.get(date)
//        return celebration ?: InternationalDay(date, context.getString(R.string.no_celebration))
        return null
    }

    fun hasCelebration(date: MonthDay): Boolean {
        return get(date) != null
    }

    fun count(criteria: String): Set<Int> {
        return delegate.count(criteria)
    }

    fun find(criteria: String): List<InternationalDay> {
        return delegate.find(criteria)
    }

    fun random(): InternationalDay {
        return delegate.random()
    }

}