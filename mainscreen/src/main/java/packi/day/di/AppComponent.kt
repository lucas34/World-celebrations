package packi.day.di

import dagger.BindsInstance
import dagger.Component
import packi.day.main.FocusCelebrationView
import packi.day.store.StoreLocator
import javax.inject.Singleton

@Singleton
@Component(modules = [FocusCelebrationViewModelProvider::class])
interface AppComponent {

    fun inject(app: FocusCelebrationView)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun storeLocator(locator: StoreLocator): Builder
        fun build(): AppComponent
    }
}