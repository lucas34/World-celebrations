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

    val store: Realm = Realm.getDefaultInstance()

    override fun loadData(context: Context) {
        if (store.isEmpty) {
            store.executeTransaction {
                store.createAllFromJson(RealmInternationalDay::class.java, context.resources.openRawResource(R.raw.celebration))
            }
        }
    }

    override fun get(date: MonthDay): InternationalDay {
        return store.where<RealmInternationalDay>()
            .equalTo("day", date.dayOfMonth)
            .equalTo("month", date.monthOfYear)
            .findFirst()
            ?.toInternationalDay() ?: InternationalDay(date, null)
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
                return result[index]!!.toInternationalDay()
            }
        }
    }

    override fun getRandom(): InternationalDay {
        val size = store.where<RealmInternationalDay>().count().toInt()

        val value = Random().nextInt(size)
        return store.where<RealmInternationalDay>().findAll()[value]!!.toInternationalDay()
    }

}