package packi.day.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import packi.day.store.DataStore
import packi.day.store.InternationalDay

class ListAllCelebrationsViewModel(private val model: DataStore) : ViewModel() {

    var filter: String = ""
        set(value) {
            field = value
            headerList.value = Pair(model.count(filter), model.find(filter))
        }

    private val headerList: MutableLiveData<Pair<Set<Int>, List<InternationalDay>>> = MutableLiveData()

    fun observeList(): LiveData<Pair<Set<Int>, List<InternationalDay>>> {
        return headerList
    }

}