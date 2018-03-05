package packi.day.store.feature.realm

import android.content.Context
import io.realm.Case
import io.realm.Realm
import io.realm.kotlin.where
import org.joda.time.MonthDay
import packi.day.store.InternationalDay
import packi.day.store.StoreDelegate
import java.util.*

class RealmStoreDelegate : StoreDelegate {

    val store = Realm.getDefaultInstance()

    override fun loadData(context: Context) {
        if (store.isEmpty) {
            store.executeTransaction {
                store.createAllFromJson(RealmInternationalDay::class.java, context.resources.openRawResource(R.raw.celebration))
            }
        }
    }

    override fun get(date: MonthDay): InternationalDay? {
        return store.where<RealmInternationalDay>()
                .equalTo("day", date.dayOfMonth)
                .equalTo("month", date.monthOfYear)
                .findFirst()
                ?.adapt()
    }

    override fun count(criteria: String): Set<Int> {
        val result = HashSet<Int>(12)

        var total = 0

        for (month in 1..12) {

            val current = store.where<RealmInternationalDay>()
                    .contains("name", criteria, Case.INSENSITIVE)
                    .equalTo("month", month)
                    .count().toInt()

            // No result for this month
            if (current == 0) continue

            // Got match for this month
            // We add the header position for the current month
            // Which is the total count of all previous celebrations + headers
            result.add(current)

            // 1 cell for the header
            total += 1

            // Number of match for the cells
            total += current
        }

        return result
    }

    override fun find(criteria: String): List<InternationalDay> {
        var query = store.where<RealmInternationalDay>()
        if (criteria.isNotBlank()) {
            query = query.contains("name", criteria, Case.INSENSITIVE)
        }

        val result = query.findAll()
        return object : AbstractList<InternationalDay>() {

            override val size: Int
                get() = result.size

            override fun get(index: Int): InternationalDay {
                return result.get(index)!!.adapt()
            }
        }
    }

    override fun random(): InternationalDay {
        val size = store.where<RealmInternationalDay>().count().toInt()

        val value = Random().nextInt(size)
        return store.where<RealmInternationalDay>().findAll().get(value)!!.adapt()
    }


}