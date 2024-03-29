package packi.day.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.analytics.GoogleAnalytics
import com.squareup.picasso.Picasso
import packi.day.common.report
import packi.day.di.DaggerAppComponent
import packi.day.di.FocusCelebrationViewModelProvider
import packi.day.store.InternationalDay
import packi.day.store.StoreLocator
import java.util.*
import javax.inject.Inject

class FocusCelebrationView : Fragment(), SwipeListener {

    private lateinit var viewModel: FocusCelebrationViewModel

    @Inject
    lateinit var focusViewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerAppComponent.builder()
            .storeLocator(requireContext().applicationContext as StoreLocator)
            .build()
            .inject(this)

        viewModel = ViewModelProviders.of(this, focusViewModelFactory).get(FocusCelebrationViewModel::class.java)

        setHasOptionsMenu(false)
    }

    override fun onResume() {
        super.onResume()
        GoogleAnalytics.getInstance(requireContext()).report("/launcher")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_focus_celebration, container, false)

        rootView.findViewById<View>(R.id.fab).setOnClickListener { v -> viewModel.setRandomDate() }
        rootView.findViewById<View>(R.id.random_button).setOnClickListener { v -> viewModel.setRandomDate() }

        val dateView: TextView = rootView.findViewById(R.id.celebration_date)
        val nameView: TextView = rootView.findViewById(R.id.celebration_name)
        val imageView: ImageView = rootView.findViewById(R.id.celebration_image)

        viewModel.observeCelebration().observe(viewLifecycleOwner) { celebration ->
            if (celebration.isToday) {
                dateView.setText(R.string.today)
            } else {
                val date = celebration.date.dayOfMonth
                val month = celebration.date.monthOfYear().getAsText(Locale.getDefault())
                dateView.text = dateView.resources.getString(R.string.date_text_title, date, month)
            }

            nameView.text = celebration.name
            Picasso.get().load(celebration.drawable).error(R.drawable.noimage).into(imageView)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener(OnSwipeListener(requireContext(), this));
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