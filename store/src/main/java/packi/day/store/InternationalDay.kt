package packi.day.store

import android.net.Uri
import org.joda.time.MonthDay
import org.json.JSONObject

data class InternationalDay(internal val id: Int,
                            val name: String,
                            internal val day: Int,
                            internal val month: Int,
                            val image: String?) : CelebrationAdaptable {

    val date: MonthDay by lazy {
        return@lazy MonthDay(day, month)
    }

    val drawable: Uri by lazy {
        if (image.isNullOrBlank()) {
            return@lazy Uri.parse("file:///android_asset/noimage.png")
        } else {
            val base = "https://raw.githubusercontent.com/lucas34/World-celebrations/modular/assets/images/"
            return@lazy Uri.parse("$base$image.png")
        }
    }

    val isToday: Boolean by lazy {
        return@lazy MonthDay.now().equals(date)
    }

    constructor(id: Int = -1, date: MonthDay, name: String, image: String? = null) :
            this(id, name, date.dayOfMonth, date.monthOfYear, image)

    constructor(json: JSONObject) : this(
            json.getInt("id"),
            json.getString("name"),
            json.getInt("day"),
            json.getInt("month"),
            json.getString("image")
    )

    override fun adapt(): InternationalDay {
        return InternationalDay(id, name, day, month, image)
    }

}