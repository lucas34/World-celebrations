package packi.day.main

import androidx.lifecycle.ViewModel
import org.joda.time.MonthDay
import packi.day.store.InternationalDay
import packi.day.store.InternationalDayRepository

class FocusCelebrationViewModel(
    private val repository: InternationalDayRepository,
) : ViewModel() {

    fun get(date: MonthDay): InternationalDay = repository.get(date)

    fun setRandomDate(): InternationalDay {
        return repository.getRandom()
    }

}