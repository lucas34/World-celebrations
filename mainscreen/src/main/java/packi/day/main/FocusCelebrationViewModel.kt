package packi.day.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import org.joda.time.MonthDay
import packi.day.store.DataStore
import packi.day.store.InternationalDay

class FocusCelebrationViewModel: ViewModel() {

    // TODO see if we can inject this
    private lateinit var model: DataStore

    private val celebration: MutableLiveData<InternationalDay> = MutableLiveData()

    fun setup(model: DataStore) {
        this.model = model
        setDate(MonthDay.now())
    }

    fun observeCelebration() : LiveData<InternationalDay> {
        return celebration
    }

    fun setDate(date: MonthDay) {
        celebration.value = model.get(date)
    }

    fun setRandomDate() {
        celebration.value = model.getRandom()
    }

}