package packi.day.main;

import android.content.Context;

import javax.inject.Scope;

import dagger.Module;
import dagger.Provides;
import packi.day.store.DataStore;
import packi.day.store.StoreDelegate;

@Module
public class DataStoreModule {

    private final Context context;
    private final StoreDelegate delegate;

    public DataStoreModule(Context context, StoreDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Provides
    public DataStore getDataStore() {
        return new DataStore(context, delegate);
    }

}
