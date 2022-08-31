package packi.day.store

import android.content.res.Resources
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import org.joda.time.MonthDay
import packi.day.store.feature.R

object DataLoader {

    private val PARSER = GsonBuilder().registerTypeAdapter(InternationalDay::class.java, JsonDeserializer<InternationalDay> { json, _, _ ->
        val jsonObject = json.asJsonObject
        val id = jsonObject["id"].asInt // : 1,
        val image = jsonObject["image"].asString // : "peace",
        val name = jsonObject["name"].asString // : "World Day of Peace"

        val dayOfMonth = jsonObject["day"].asInt // : 1,
        val monthOfYear = jsonObject["month"].asInt // : 1,

        InternationalDay(MonthDay(monthOfYear, dayOfMonth), Celebration(id, name, image))
    }).create()

    fun load(resources: Resources): Array<InternationalDay> {
        val reader = resources.openRawResource(R.raw.celebration).reader()
        return PARSER.fromJson(reader, Array<InternationalDay>::class.java)
    }

}