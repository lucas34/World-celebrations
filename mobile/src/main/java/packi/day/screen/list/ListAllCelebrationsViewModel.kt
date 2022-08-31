package packi.day.screen.list

import androidx.lifecycle.ViewModel
import packi.day.store.InternationalDayRepository

class ListAllCelebrationsViewModel(
    private val model: InternationalDayRepository
) : ViewModel() {

    fun getData(filter: String) = model.find(filter)

}