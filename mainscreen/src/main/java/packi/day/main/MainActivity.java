package packi.day.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import packi.day.store.DataStore;
import packi.day.store.feature.HashMapStoreDelegate;

public class MainActivity extends AppCompatActivity implements StoreLocator {

    private DataStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        store = new DataStore(getApplicationContext(), new HashMapStoreDelegate());

        Toolbar viewById = findViewById(R.id.toolbar);
        viewById.setTitle(R.string.app_name);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new FocusCelebrationFragment());
        ft.commit();
    }

    @Override
    public DataStore getStore() {
        return store;
    }

}
