package packi.day.store.feature.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.joda.time.MonthDay
import packi.day.store.Celebration
import packi.day.store.InternationalDay

/**
 * As of Realm 4.3.0
 * Data class is not supported
 * Class has to be open for the proxy generation
 * 👎 for Realm
 */
open class RealmInternationalDay : RealmObject() {

    @PrimaryKey
    var id: Int = 0

    var name: String? = null

    var day: Int = 0

    var month: Int = 0

    var image: String? = null

}

fun RealmInternationalDay.toInternationalDay(): InternationalDay {
    return InternationalDay(MonthDay(month, day), Celebration(id, name.orEmpty(), image))
}