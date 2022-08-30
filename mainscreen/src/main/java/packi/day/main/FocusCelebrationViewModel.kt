package packi.day.main

import androidx.lifecycle.ViewModel
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import org.joda.time.MonthDay
import packi.day.store.InternationalDayRepository

class FocusCelebrationViewModel(
    private val repository: InternationalDayRepository,
    private val args: Bundle? = null
) : ViewModel() {

    private val state by lazy { mutableStateOf(repository.get((args?.getSerializable("date") as MonthDay?) ?: MonthDay.now())) }

    val current by state

    private fun updateState(date: MonthDay) {
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