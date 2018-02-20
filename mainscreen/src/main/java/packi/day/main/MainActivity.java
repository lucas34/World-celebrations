package packi.day.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import packi.day.store.StoreData;
import packi.day.store.feature.HashMapDayStore;

public class MainActivity extends AppCompatActivity implements StoreLocator {

    private StoreData storeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storeData = new StoreData(getApplicationContext(), new HashMapDayStore());

        Toolbar viewById = findViewById(R.id.toolbar);
        viewById.setTitle(R.string.app_name);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new FocusCelebrationFragment());
        ft.commit();
    }

    @Override
    public StoreData getStore() {
        return storeData;
    }

}
