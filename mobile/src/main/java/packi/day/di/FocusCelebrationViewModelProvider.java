package packi.day.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import packi.day.main.FocusCelebrationViewModel;
import packi.day.store.StoreLocator;

@Module
public class FocusCelebrationViewModelProvider {

    @Provides
    ViewModelProvider.Factory provideFocusCelebrationViewModel(StoreLocator locator) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new FocusCelebrationViewModel(locator.getStore(), null);
            }
        };
    }

}
