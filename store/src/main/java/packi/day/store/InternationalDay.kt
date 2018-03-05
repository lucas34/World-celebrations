package packi.day.store

import android.graphics.drawable.Drawable
import android.net.Uri
import org.joda.time.MonthDay
import org.json.JSONObject

/**
 * Created by lucasnelaupe on 5/3/18.
 */

data class InternationalDay(val id: Int,
                            val name: String,
                            val day: Int,
                            val month: Int,
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

    constructor(id : Int = -1, date: MonthDay, name: String, image: String? = null) :
            this(id, name, date.dayOfMonth, date.monthOfYear, image)

    constructor(json: JSONObject) : this(
            json.getInt("id"),
            json.getString ("name"),
            json.getInt("day"),
            json.getInt("month"),
            json.getString("image")
    )

    override fun adapt(): InternationalDay {
        return InternationalDay(id, name, day, month, image)
    }

}