package packi.day

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

import com.google.android.gms.analytics.GoogleAnalytics
import packi.day.common.report

class NotificationActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val text = findViewById<TextView>(R.id.text_view)
        text.text = intent?.getStringExtra(EXTRA_TITLE)
    }

    override fun onResume() {
        super.onResume()
        GoogleAnalytics.getInstance(applicationContext).report("/wear/notification")
    }

    private companion object {
        const val EXTRA_TITLE = "title"
    }
}