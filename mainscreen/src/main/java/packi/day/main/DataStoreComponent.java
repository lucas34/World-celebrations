package packi.day.main;

import android.support.v4.app.Fragment;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import packi.day.main.DataStoreModule;
import packi.day.store.DataStore;

@Component(modules = DataStoreModule.class)
public interface DataStoreComponent {

    DataStore getDataStore();

}