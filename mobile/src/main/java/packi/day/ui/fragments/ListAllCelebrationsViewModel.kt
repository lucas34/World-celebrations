package packi.day.ui.fragments

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.joda.time.MonthDay
import packi.day.store.InternationalDayRepository
import packi.day.store.InternationalDay

class ListAllCelebrationsViewModel(private val model: InternationalDayRepository) : ViewModel() {

    private val state by lazy { mutableStateOf(model.find(filter)) }

    var filter: String = ""
        set(value) {
            field = value
            state.value = model.find(value)
        }

}