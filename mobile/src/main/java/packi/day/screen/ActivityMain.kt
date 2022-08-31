package packi.day.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.joda.time.MonthDay
import packi.day.R
import packi.day.WorldApplication
import packi.day.main.FocusCelebrationView
import packi.day.store.InternationalDayRepository
import packi.day.store.StoreLocator
import packi.day.screen.list.ListAllCelebrationsView

class ActivityMain : ComponentActivity(), StoreLocator {

    override val store: InternationalDayRepository
        get() {
            val application = application as WorldApplication
            return application.celebrationHelper
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScafold()
        }

//        navigationHandler.setOnFragmentChangeListener { _ ->
//            Keyboard.hide(this@ActivityMain)
//        }

//        if (savedInstanceState == null) {
//            navigationHandler.showMain(FocusCelebrationView())
//        }

//        val toolbar = findViewById<Toolbar>(R.id.toolbar)

//        setSupportActionBar(toolbar)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home_black_24dp)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        findViewById<NavigationView>(R.id.navigation_view)?.setNavigationItemSelectedListener { menuItem ->
//            //            menuItem.isChecked = false
//            drawerLayout.closeDrawers()
//
//            when (menuItem.itemId) {
//                R.id.menu_home -> {
//                    navigationHandler.showMain(FocusCelebrationView())
//                    return@setNavigationItemSelectedListener true
//                }
//
//                R.id.menu_list_all -> {
//                    navigationHandler.replaceContent(ListAllCelebrationsView())
//                    return@setNavigationItemSelectedListener true
//                }
//
//                R.id.menu_settings -> {
//                    navigationHandler.replaceContent(PreferencesFragment())
//                    return@setNavigationItemSelectedListener true
//                }
//
//                R.id.menu_google_play -> {
//                    launchStoreApp(applicationContext)
//                    return@setNavigationItemSelectedListener true
//                }
//
//                R.id.menu_pick_date -> {
//                    val picker = MaterialDatePicker.Builder.datePicker().build()
//                    picker.addOnPositiveButtonClickListener {
//                        showFocus(MonthDay.fromDateFields(Date(it)))
//                    }
//                    picker.show(supportFragmentManager, "Calendar")
//                    return@setNavigationItemSelectedListener true
//                }
//
//                R.id.menu_info -> {
//                    info()
//                    return@setNavigationItemSelectedListener true
//                }
//
//                else -> return@setNavigationItemSelectedListener false
//            }
//        }

//        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
//        drawerLayout.addDrawerListener(actionBarDrawerToggle)
//        actionBarDrawerToggle.syncState()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
//                drawerLayout.openDrawer(GravityCompat.START)
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


    fun setScreenTitle(title: Int) {
//        val supportActionBar = supportActionBar
//        supportActionBar?.setTitle(title)
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

@Composable
fun BottomBar(
    navController: NavController
) {
    val selectedIndex = remember { mutableStateOf(0) }
    BottomNavigation(elevation = 10.dp) {
        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home,"")
        },
            label = { Text(text = "Today") },
            selected = (selectedIndex.value == 0),
            onClick = {
                selectedIndex.value = 0
                navController.navigate("main")
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.List,"")
        },
            label = { Text(text = "List All") },
            selected = (selectedIndex.value == 1),
            onClick = {
                selectedIndex.value = 1
                navController.navigate("listAll")
            })

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Settings,"")
        },
            label = { Text(text = "Settings") },
            selected = (selectedIndex.value == 2),
            onClick = {
                selectedIndex.value = 2
                navController.navigate("settings")
            })
    }
}


@Composable
fun AppScafold(
) {
    val navController: NavHostController = rememberNavController()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = {Text(stringResource(id = R.string.app_name))})  },
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        MyAppNavHost(modifier = Modifier.padding(padding), navController = navController)
    }
}

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = "main",
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable("main") {
            FocusCelebrationView()
        }
        composable("main/{date}", arguments = listOf(navArgument("date") { type = NavType.StringType })) { backStackEntry ->
            FocusCelebrationView(backStackEntry.arguments?.getString("date").toMonthDay())
        }
        composable("listAll") {
            ListAllCelebrationsView {
                navController.navigate("main/${it.date}")
            }
        }
        composable("settings") {

        }
    }
}

fun String?.toMonthDay(): MonthDay {
    return try {
        MonthDay.parse(this)
    } catch (e: Exception) {
        MonthDay.now()
    }
}