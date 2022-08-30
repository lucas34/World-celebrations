package packi.day

import android.app.Activity
import android.os.Bundle
import android.support.wearable.view.CardScrollView
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.analytics.GoogleAnalytics
import com.squareup.picasso.Picasso
import org.joda.time.MonthDay
import packi.day.common.report
import packi.day.store.InternationalDayRepository


class MainActivity: Activity() {

    lateinit var store: InternationalDayRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store = InternationalDayRepository(applicationContext)

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
        GoogleAnalytics.getInstance(applicationContext).report("/wear/launcher")
    }

}