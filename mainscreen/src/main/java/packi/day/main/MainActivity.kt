package packi.day.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.google.android.gms.analytics.GoogleAnalytics
import packi.day.common.report

import packi.day.store.DataStore
import packi.day.store.StoreLocator

/// Class is actually registered
@SuppressLint("Registered")
class MainActivity : AppCompatActivity(), StoreLocator {

    override lateinit var store: DataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store = DataStore(applicationContext)

        val viewById = findViewById<Toolbar>(R.id.toolbar)
        viewById.setTitle(R.string.app_name)

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, FocusCelebrationFragment())
                .commit()
    }

    override fun onResume() {
        super.onResume()
        GoogleAnalytics.getInstance(applicationContext).report("/instantApp")
    }
}