package packi.day.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.joda.time.MonthDay
import packi.day.store.InternationalDayRepository

class FocusCelebrationViewModel(
    private val repository: InternationalDayRepository,
) : ViewModel() {

    private val state by lazy { mutableStateOf(repository.get(MonthDay.now())) }

    val current by state

    fun updateState(date: MonthDay) {
        state.value = repository.get(date)
    }

    fun plusDays(days: Int) {
        updateState(state.value.date.plusDays(days))
    }

    fun plusMonths(months: Int) {
        updateState(state.value.date.plusMonths(months))
    }

    fun setRandomDate() {
        state.value = repository.getRandom()
    }

}