package packi.day

import android.app.Activity
import android.os.Bundle
import android.support.wearable.view.CardScrollView
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.joda.time.MonthDay
import packi.day.common.AnalyticsTracker
import packi.day.store.DataStore


class MainActivity: Activity() {

    lateinit var store: DataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store = DataStore(applicationContext)

        val cardScrollView = findViewById<CardScrollView>(R.id.card_scroll_view)
        cardScrollView.cardGravity = Gravity.BOTTOM

        val now = MonthDay.now()
        val celebration = store.get(now)

        val image = findViewById<ImageView>(R.id.IllustrationJournee)
        Picasso.with(this).load(celebration.drawable).into(image)

        val textView = findViewById<TextView>(R.id.text)
        textView.setText(celebration.name)
    }

    override fun onResume() {
        super.onResume()
        AnalyticsTracker.getInstance(applicationContext).sendTracker("/wear/launcher")
    }

}