package packi.day.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
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
import packi.day.store.StoreLocator
import java.util.*

class FocusCelebrationView : Fragment(), SwipeListener {

    private lateinit var viewModel: FocusCelebrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val locator = activity as StoreLocator?
                return FocusCelebrationViewModel(locator!!.store, arguments) as T
            }
        }).get(FocusCelebrationViewModel::class.java)

        setHasOptionsMenu(false)
    }

    override fun onResume() {
        super.onResume()
        GoogleAnalytics.getInstance(context).report("/launcher")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_focus_celebration, container, false)

        rootView.findViewById<View>(R.id.fab).setOnClickListener { v -> viewModel.setRandomDate() }
        rootView.findViewById<View>(R.id.random_button).setOnClickListener { v -> viewModel.setRandomDate() }

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