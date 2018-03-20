package packi.day.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.analytics.GoogleAnalytics
import com.squareup.picasso.Picasso
import packi.day.common.report
import packi.day.store.InternationalDay
import java.util.*
import javax.inject.Inject

class FocusCelebrationView : Fragment(), SwipeListener {

    @Inject
    lateinit var viewModel: FocusCelebrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locator = activity as DataStoreComponentLocator

        DaggerFocusCelebrationComponent.builder()
                .dataStoreComponent(locator.dataStoreComponent())
                .viewModelModule(ViewModelModule(this))
                .build()
                .inject(this)

        setHasOptionsMenu(false)
    }

    override fun onResume() {
        super.onResume()
        GoogleAnalytics.getInstance(context).report("/launcher")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_focus_celebration, container, false)

        rootView.findViewById<View>(R.id.fab).setOnClickListener { _ -> viewModel.setRandomDate() }
        rootView.findViewById<View>(R.id.random_button).setOnClickListener { _ -> viewModel.setRandomDate() }

        val dateView: TextView = rootView.findViewById(R.id.celebration_date)
        val nameView: TextView = rootView.findViewById(R.id.celebration_name)
        val imageView: ImageView = rootView.findViewById(R.id.celebration_image)

        viewModel.observeCelebration().observe(this, Observer<InternationalDay> { celebration ->
            // Unwrap. Not sure why celebration can be null in this case
            val celebration = celebration ?: return@Observer

            if (celebration.isToday) {
                dateView.setText(R.string.today)
            } else {
                val date = celebration.date.dayOfMonth
                val month = celebration.date.monthOfYear().getAsText(Locale.getDefault())
                dateView.setText(dateView.resources.getString(R.string.date_text_title, date, month))
            }

            nameView.setText(celebration.name)
            Picasso.with(rootView.context).load(celebration.drawable).error(R.drawable.noimage).into(imageView)
        })

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener(OnSwipeListener(context!!, this));
    }

    override fun onSwipe(direction: Direction) {
        when (direction) {
            Direction.LEFT -> viewModel.plusDays(-1)
            Direction.RIGHT -> viewModel.plusDays(1)
            Direction.UP -> viewModel.plusMonths(1)
            Direction.DOWN -> viewModel.plusMonths(-1)
        }
    }

}