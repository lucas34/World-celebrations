package packi.day.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.Bundle
import org.joda.time.MonthDay
import packi.day.store.DataStore
import packi.day.store.InternationalDay

class FocusCelebrationViewModel(private val model: DataStore, private val args: Bundle? = null) : ViewModel() {

    private val celebration: MutableLiveData<InternationalDay> = MutableLiveData()

    init {
        setDate(getDateParam() ?: MonthDay.now())
    }

    private val currentDate: MonthDay
        get() {
            return celebration.value?.date ?: MonthDay.now()
        }

    private fun getDateParam(): MonthDay? {
        return args?.getSerializable("date") as MonthDay?
    }

    fun observeCelebration(): LiveData<InternationalDay> {
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