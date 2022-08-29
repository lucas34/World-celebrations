package packi.day.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import packi.day.main.FocusCelebrationViewModel
import packi.day.store.StoreLocator

@Module
object FocusCelebrationViewModelProvider {

    @Provides
    fun provideFocusCelebrationViewModel(locator: StoreLocator): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FocusCelebrationViewModel(locator.store, null) as T
            }
        }
    }
}