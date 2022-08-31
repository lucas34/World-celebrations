package packi.day.di

import dagger.BindsInstance
import dagger.Component
import packi.day.main.FocusCelebrationViewModel
import packi.day.store.StoreLocator
import javax.inject.Singleton

@Singleton
@Component(modules = [FocusCelebrationViewModelProvider::class])
abstract class FocusCelebrationComponent {

    abstract fun getViewModel() : FocusCelebrationViewModel

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun storeLocator(locator: StoreLocator): Builder
        fun build(): FocusCelebrationComponent
    }
}