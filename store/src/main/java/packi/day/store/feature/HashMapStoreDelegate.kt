package packi.day.store.feature

import android.content.Context
import org.joda.time.MonthDay
import org.json.JSONArray
import org.json.JSONException
import packi.day.store.InternationalDay
import packi.day.store.StoreDelegate
import java.io.InputStream
import java.util.*

class HashMapStoreDelegate : StoreDelegate {

    var store: HashMap<MonthDay, InternationalDay> = HashMap(365)

    override fun loadData(context: Context) {
        fillData(context.resources.openRawResource(R.raw.celebration))
    }

    override fun get(date: MonthDay): InternationalDay {
        return store[date]!!
    }

    override fun count(criteria: String): Set<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun find(criteria: String): List<InternationalDay> {
        val results = ArrayList<InternationalDay>(365)
        for (value in store.values) {
            if (value.celebration?.name?.contains(criteria) == true) {
                results.add(value)
            }
        }
        return results
    }

    override fun getRandom(): InternationalDay {
        var data: InternationalDay?
        do {
            data = get(randomDate())
        } while (data == null)
        return data
    }

    private fun randomInt(random: Random, lower: Int, upper: Int): Int {
        return random.nextInt(upper - lower + 1) + lower
    }

    private fun randomDate(): MonthDay {
        val random = Random()

        val monthRandom = randomInt(random, 1, 12)

        val calendar = GregorianCalendar(0, monthRandom - 1, 1)

        val dayRandom = randomInt(random, 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        return MonthDay(monthRandom, dayRandom)
    }

    private fun fillData(stream: InputStream) {
        var scanner: Scanner? = null
        try {
            scanner = getFullStringScanner(stream)
            val jsonArray = JSONArray(scanner.next())
            for (i in 0 until jsonArray.length()) {
//                val jsonobject = jsonArray.getJSONObject(i)
//                val internationalDay = InternationalDay(jsonobject)
//                store[internationalDay.date] = internationalDay
            }
        } catch (e: JSONException) {
            throw RuntimeException("Failed to read JSON", e)
        } finally {
            if (scanner != null) {
                scanner.close()
            }
        }
    }

    private fun getFullStringScanner(`in`: InputStream): Scanner {
        return Scanner(`in`, "UTF-8").useDelimiter("\\A")
    }

}