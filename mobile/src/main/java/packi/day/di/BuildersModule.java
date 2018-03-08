package packi.day.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import packi.day.main.FocusCelebrationView;

@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector(modules = FocusCelebrationViewModelProvider.class)
    abstract FocusCelebrationView bindProviderFragment();

}

