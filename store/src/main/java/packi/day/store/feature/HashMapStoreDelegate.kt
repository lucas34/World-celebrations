package packi.day.store.feature

import android.content.Context
import org.joda.time.MonthDay
import packi.day.store.Celebration
import packi.day.store.DataLoader
import packi.day.store.InternationalDay
import packi.day.store.StoreDelegate

class HashMapStoreDelegate : StoreDelegate {

    private var store: Map<MonthDay, Celebration?> = emptyMap()

    override fun loadData(context: Context) {
        store = DataLoader.load(context.resources).associate { it.date to it.celebration }
    }

    override fun get(date: MonthDay): InternationalDay {
        return InternationalDay(date, store[date])
    }

    override fun find(criteria: String): List<InternationalDay> {
//        val results = ArrayList<InternationalDay>(store.size)
//        for (value in store.values) {
//            if (value?.name?.contains(criteria) == true) {
//                results.add(InternationalDay(value))
//            }
//        }
//        return results
        return emptyList()
    }

    override fun getRandom(): InternationalDay {
//        return store.values.stream().findAny().get()
        return InternationalDay(MonthDay.now(), null)
    }

}