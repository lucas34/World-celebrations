package packi.day.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.joda.time.MonthDay
import packi.day.R
import packi.day.WorldApplication
import packi.day.common.Keyboard
import packi.day.main.FocusCelebrationView
import packi.day.store.InternationalDayRepository
import packi.day.store.StoreLocator
import packi.day.ui.fragments.ListAllCelebrationsView

class ActivityMain : ComponentActivity(), StoreLocator {

//    private lateinit var navigationHandler: SupportNavigationHandler<Fragment>
//    private lateinit var drawerLayout: DrawerLayout

    override val store: InternationalDayRepository
        get() {
            val application = application as WorldApplication
            return application.celebrationHelper
        }

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        // Init lateinit var
//        navigationHandler = SupportNavigationHandler(fragmentManager, R.id.content)
//        drawerLayout = findViewById(R.id.drawer)

        setContent {
            MyAppNavHost(this)
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

    fun showFocus(date: MonthDay) {
        val data = Intent()
        val args = Bundle()
        args.putSerializable("date", date)
        data.putExtras(args)
//        navigationHandler.showMain(FocusCelebrationView(), args)
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppScafold() {
    BackdropScaffold(
        appBar = {
            TopAppBar(
//                title = { Text("Backdrop") },
//                navigationIcon = {
//                    if (false) {
//                        IconButton(
//                            onClick = {
////                                scope.launch { scaffoldState.reveal() }
//                            }
//                        ) {
//                            Icon(
//                                Icons.Default.Menu,
//                                contentDescription = "Menu"
//                            )
//                        }
//                    } else {
//                        IconButton(
//                            onClick = {
////                                scope.launch { scaffoldState.conceal() }
//                            }
//                        ) {
//                            Icon(
//                                Icons.Default.Close,
//                                contentDescription = "Close"
//                            )
//                        }
//                    }
//                },
//                elevation = 0.dp,
//                backgroundColor = Color.Transparent
            ) {

            }
        },
        backLayerContent = {
            // Back layer content
        },
        frontLayerContent = {
            // Front layer content
        }
    )
}



@Composable
fun MyAppNavHost(
    context: Context,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "listAll",
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
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
    }
}

fun String?.toMonthDay(): MonthDay {
    return try {
        MonthDay.parse(this)
    } catch (e: Exception) {
        MonthDay.now()
    }
}