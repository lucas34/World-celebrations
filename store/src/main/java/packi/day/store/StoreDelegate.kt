package packi.day.store

import android.content.Context
import org.joda.time.MonthDay

/**
 * Created by lucasnelaupe on 5/3/18.
 */

interface StoreDelegate {

    fun loadData(context: Context)

    fun get(date: MonthDay): InternationalDay?

    fun count(criteria: String): Set<Int>

    fun find(criteria: String): List<InternationalDay>

    fun random(): InternationalDay

}