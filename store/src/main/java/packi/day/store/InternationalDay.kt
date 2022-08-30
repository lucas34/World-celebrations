package packi.day.store

import android.net.Uri
import org.joda.time.MonthDay

data class InternationalDay(
    val date: MonthDay,
    val celebration: Celebration?
) {

    companion object {
        fun today(store: InternationalDayRepository): InternationalDay {
            return store.get(MonthDay.now())
        }
    }

}

data class Celebration(
    val id: Int,
    val name: String,
    val image: String?
) {

    val drawable: Uri by lazy {
        if (image.isNullOrBlank()) {
            return@lazy Uri.parse("file:///android_asset/noimage.png")
        } else {
            val base = "https://raw.githubusercontent.com/lucas34/World-celebrations/modular/assets/images/"
            return@lazy Uri.parse("$base$image.png")
        }
    }

}