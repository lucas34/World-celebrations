package packi.day.ui.fragments

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import packi.day.store.CelebrationComponent
import packi.day.store.DataStore
import javax.inject.Inject

class ListAllCelebrationsViewModel : ViewModel() {

    @Inject
    lateinit var model: DataStore

    var filter: String = ""
        set(value) {
            field = value
            headerCelebration.value = model.find(filter)
        }

    private val headerCelebration: MutableLiveData<List<CelebrationComponent>> = MutableLiveData()

    fun observeList(): LiveData<List<CelebrationComponent>> {
        return headerCelebration
    }

}