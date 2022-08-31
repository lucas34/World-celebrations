package packi.day.store

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
    private val image: String?
) {

    val path
        get() = "https://raw.githubusercontent.com/lucas34/World-celebrations/master/assets/images/$image.png"

}