package packi.day.di;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import packi.day.WorldApplication;
import packi.day.store.StoreLocator;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        BuildersModule.class})
public interface AppComponent {

    void inject(WorldApplication app);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder storeLocator(StoreLocator locator);

        AppComponent build();
    }
}

