package packi.day.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.joda.time.MonthDay;

import packi.day.R;
import packi.day.WorldApplication;
import packi.day.lib.SupportNavigationHandler;
import packi.day.main.FocusCelebrationFragment;
import packi.day.main.StoreLocator;
import packi.day.store.StoreData;
import packi.day.ui.fragments.CustomDateSearchBuilder;
import packi.day.ui.fragments.ListAllCelebrationFragment;
import packi.day.ui.fragments.PreferencesFragment;

public class ActivityMain extends AppCompatActivity implements StoreLocator {

    private SupportNavigationHandler<Fragment> navigationHandler;
    private DrawerLayout drawerLayout;

    private static boolean isNotLaunchedActivity(Activity activity, Intent intent) {
        try {
            activity.startActivity(intent);
            return false;
        } catch (ActivityNotFoundException e) {
            return true;
        }
    }

    private void launchMarket() {
        String packageName = getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        if (isNotLaunchedActivity(this, intent)) {
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            if (isNotLaunchedActivity(this, intent)) {
                Toast.makeText(this, getString(R.string.error_market), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationHandler = new SupportNavigationHandler<>(this, R.id.content);
        navigationHandler.setOnFragmentChangeListener(fragment -> {
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        if (savedInstanceState == null) {
            navigationHandler.showMain(new FocusCelebrationFragment());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_home_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {

            menuItem.setChecked(false);
            drawerLayout.closeDrawers();

            switch (menuItem.getItemId()) {

                case R.id.menu_home:
                    navigationHandler.showMain(new FocusCelebrationFragment());
                    return true;

                case R.id.menu_list_all:
                    navigationHandler.replaceContent(new ListAllCelebrationFragment());
                    return true;

                case R.id.menu_settings:
                    navigationHandler.replaceContent(new PreferencesFragment());
                    return true;

                case R.id.menu_google_play:
                    launchMarket();
                    return true;

                case R.id.menu_pick_date:
                    new CustomDateSearchBuilder(this).show();
                    return true;

                case R.id.menu_info:
                    info();
                    return true;

                default:
                    return false;

            }
        });

        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void info() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.app_info);
        builder.setPositiveButton(R.string.ok, null);
        builder.show();
    }

    public void showFocus(MonthDay date) {
        Intent data = new Intent();
        Bundle args = new Bundle();
        args.putSerializable("date", date);
        data.putExtras(args);
        navigationHandler.replaceContent(new FocusCelebrationFragment(), args);
    }

    public void setScreenTitle(int title) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    @Override
    public StoreData getStore() {
        WorldApplication application = (WorldApplication) getApplication();
        return application.getCelebrationHelper();
    }
}
