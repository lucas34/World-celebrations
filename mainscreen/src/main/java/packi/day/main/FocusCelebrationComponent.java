package packi.day.main;

import dagger.Component;

@Component(modules = ViewModelModule.class, dependencies = DataStoreComponent.class)
public interface FocusCelebrationComponent {

    void inject(FocusCelebrationView focusCelebrationView);

}
