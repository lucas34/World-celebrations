package packi.day.main

import androidx.lifecycle.ViewModel
import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.joda.time.MonthDay
import packi.day.store.DataStore
import packi.day.store.InternationalDay

class FocusCelebrationViewModel(private val model: DataStore, private val args: Bundle? = null) : ViewModel() {

    private val celebration: MutableState<InternationalDay> = mutableStateOf(model.get(MonthDay.now()))

    init {
        setDate(getDateParam() ?: MonthDay.now())
    }

    private val currentDate: MonthDay
        get() {
            return celebration.value.date
        }

    private fun getDateParam(): MonthDay? {
        return args?.getSerializable("date") as MonthDay?
    }

    fun observeCelebration(): MutableState<InternationalDay> {
        return celebration
    }

    fun setDate(date: MonthDay) {
        celebration.value = model.get(date)
    }

    fun plusDays(days: Int) {
        setDate(currentDate.plusDays(days))
    }

    fun plusMonths(months: Int) {
        setDate(currentDate.plusMonths(1))
    }

    fun setRandomDate() {
        celebration.value = model.getRandom()
    }

}