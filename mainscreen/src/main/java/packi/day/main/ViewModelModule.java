package packi.day.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import packi.day.store.DataStore;

@Module
public class ViewModelModule {

    private final Fragment fragment;

    public ViewModelModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    ViewModelProvider.Factory getProvider(DataStore store) {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new FocusCelebrationViewModel(store, fragment.getArguments());
            }
        };
    }

    @Provides
    FocusCelebrationViewModel getViewModel(ViewModelProvider.Factory factory) {
        return ViewModelProviders.of(fragment, factory).get(FocusCelebrationViewModel.class);
    }

}
