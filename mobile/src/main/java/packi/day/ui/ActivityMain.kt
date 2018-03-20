package packi.day.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import org.joda.time.MonthDay
import packi.day.R
import packi.day.WorldApplication
import packi.day.common.Keyboard
import packi.day.lib.SupportNavigationHandler
import packi.day.main.DataStoreComponent
import packi.day.main.DataStoreComponentLocator
import packi.day.main.FocusCelebrationView
import packi.day.ui.fragments.CustomDateSearchBuilder
import packi.day.ui.fragments.ListAllCelebrationsView
import packi.day.ui.fragments.PreferencesFragment

class ActivityMain : AppCompatActivity(), DataStoreComponentLocator {

    private lateinit var navigationHandler: SupportNavigationHandler<Fragment>
    private lateinit var drawerLayout: DrawerLayout

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Keyboard.hide(this)
        if (intent.hasExtra("date")) {
            val serializable = intent.getSerializableExtra("date")
            if (serializable != null) {
                val date = serializable as MonthDay
                showFocus(date)
            }
        }
    }

    private fun launchMarket() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init lateinit var
        navigationHandler = SupportNavigationHandler(this, R.id.content)
        drawerLayout = findViewById(R.id.drawer)

        navigationHandler.setOnFragmentChangeListener { _ ->
            Keyboard.hide(this@ActivityMain)
        }

        if (savedInstanceState == null) {
            navigationHandler.showMain(FocusCelebrationView())
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home_black_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<NavigationView>(R.id.navigation_view)?.setNavigationItemSelectedListener { menuItem ->
            //            menuItem.isChecked = false
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.menu_home -> {
                    navigationHandler.showMain(FocusCelebrationView())
                    return@setNavigationItemSelectedListener true
                }

                R.id.menu_list_all -> {
                    navigationHandler.replaceContent(ListAllCelebrationsView())
                    return@setNavigationItemSelectedListener true
                }

                R.id.menu_settings -> {
                    navigationHandler.replaceContent(PreferencesFragment())
                    return@setNavigationItemSelectedListener true
                }

                R.id.menu_google_play -> {
                    launchStoreApp(applicationContext)
                    return@setNavigationItemSelectedListener true
                }

                R.id.menu_pick_date -> {
                    CustomDateSearchBuilder(this).show()
                    return@setNavigationItemSelectedListener true
                }

                R.id.menu_info -> {
                    info()
                    return@setNavigationItemSelectedListener true
                }

                else -> return@setNavigationItemSelectedListener false
            }
        }

        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun info() {
        val builder = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
        builder.setTitle(R.string.app_name)
        builder.setMessage(R.string.app_info)
        builder.setPositiveButton(R.string.ok, null)
        builder.show()
    }

    fun showFocus(date: MonthDay) {
        val data = Intent()
        val args = Bundle()
        args.putSerializable("date", date)
        data.putExtras(args)
        navigationHandler.showMain(FocusCelebrationView(), args)
    }

    fun setScreenTitle(title: Int) {
        val supportActionBar = supportActionBar
        supportActionBar?.setTitle(title)
    }

    override fun dataStoreComponent(): DataStoreComponent {
        return WorldApplication.getDataStoreComponent(applicationContext)
    }

    companion object {

        fun launchStoreApp(context: Context) {
            val packageName = context.packageName
            val intent = Intent(Intent.ACTION_VIEW)

            // Try opening market
            intent.data = Uri.parse("market://details?id=$packageName")
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                return
            }

            // Open Google play link
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                return
            }

            // Could not find any link to open
            Toast.makeText(context, context.getString(R.string.error_market), Toast.LENGTH_SHORT).show()
        }

    }
}
