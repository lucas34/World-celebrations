package packi.day.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.analytics.GoogleAnalytics
import packi.day.common.report

import packi.day.store.InternationalDayRepository
import packi.day.store.StoreLocator

/// Class is actually registered
@SuppressLint("Registered")
class MainActivity : AppCompatActivity(), StoreLocator {

    override lateinit var store: InternationalDayRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store = InternationalDayRepository(applicationContext)

        val viewById = findViewById<Toolbar>(R.id.toolbar)
        viewById.setTitle(R.string.app_name)

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, FocusCelebrationView())
                .commit()
    }

    override fun onResume() {
        super.onResume()
        GoogleAnalytics.getInstance(applicationContext).report("/instantApp")
    }
}
