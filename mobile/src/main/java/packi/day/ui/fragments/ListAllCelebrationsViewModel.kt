package packi.day.ui.fragments

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import packi.day.store.DataStore
import packi.day.store.InternationalDay

class ListAllCelebrationsViewModel: ViewModel() {

    // TODO see if we can inject this
    private lateinit var model: DataStore

    private var filter: String? = null

    fun setup(model: DataStore) {
        this.model = model

        if (headerList.value == null) {
            // Emit default value
            setFilter(null)
        }
    }

    private val headerList: MutableLiveData<Pair<Set<Int>, List<InternationalDay>>> = MutableLiveData()

    fun observeList() : LiveData<Pair<Set<Int>, List<InternationalDay>>> {
        return headerList
    }

    fun getFilter(): String? {
        // TODO can be change to private set and public get
        // See if I can use live data and headerlist will react from this change
        return filter
    }

    fun setFilter(filter: String?) {
        this.filter = filter
        headerList.value = Pair(model.count(filter), model.find(filter))
    }

}