package packi.day.store.feature.realm

import android.content.Context
import io.realm.Case
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import org.joda.time.MonthDay
import packi.day.store.InternationalDay
import packi.day.store.CelebrationComponent
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

    private fun count(criteria: String): Set<Int> {
        val result = HashSet<Int>(12)

        var total = 0

        var query = store.where<RealmInternationalDay>()
        if (criteria.isNotBlank()) {
            query = query.contains("name", criteria, Case.INSENSITIVE)
        }

        (1..12).map {
            return@map query.equalTo("month", it).count().toInt()
        }.filter { it > 0 }.forEach {
            // Got match for this month
            // We add the header position for the current month
            // Which is the total count of all previous celebrations + headers
            result.add(it)

            // 1 cell for the header
            total += 1

            // Number of match for the cells
            total += it
        }

        return result
    }

    override fun find(criteria: String): List<CelebrationComponent> {
        val headersPositions = count(criteria = criteria)

        var query = store.where<RealmInternationalDay>()
        if (criteria.isNotBlank()) {
            query = query.contains("name", criteria, Case.INSENSITIVE)
        }

        val result = query.findAll()
        return object : AbstractList<CelebrationComponent>() {

            override val size: Int
                get() = result.size + headersPositions.count()

            override fun get(index: Int): CelebrationComponent {
                if (headersPositions.contains(index)) {
                    val next = get(index + 1) as CelebrationComponent.Celebration
                    return CelebrationComponent.Header(next.data.date)
                } else {
                    var actualIndex = index
                    for (position in headersPositions) {
                        if (position > index) {
                            actualIndex--;
                        }
                    }
                    return CelebrationComponent.Celebration(result.get(actualIndex)!!.adapt())
                }
            }
        }
    }

    override fun getRandom(): InternationalDay {
        val size = store.where<RealmInternationalDay>().count().toInt()

        val value = Random().nextInt(size)
        return store.where<RealmInternationalDay>().findAll().get(value)!!.adapt()
    }

    companion object {
        private const val REALM_VERSION = 4L

        fun init(context: Context) {
            Realm.init(context)

            val config = RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .schemaVersion(RealmStoreDelegate.REALM_VERSION)
                    .build()

            Realm.setDefaultConfiguration(config)
        }

    }

}

