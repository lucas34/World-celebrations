package packi.day.store

import android.content.Context
import org.joda.time.MonthDay

interface StoreDelegate {

    fun loadData(context: Context)

    fun get(date: MonthDay): InternationalDay?

    fun find(criteria: String): List<CelebrationComponent>

    fun getRandom(): InternationalDay

}