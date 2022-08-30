package packi.day.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso3.Picasso
import com.squareup.picasso3.compose.rememberPainter
import org.joda.time.MonthDay
import packi.day.di.DaggerAppComponent
import packi.day.image.PicassoHolder
import packi.day.store.Celebration
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

        viewModel = ViewModelProvider(this, focusViewModelFactory)[FocusCelebrationViewModel::class.java]
        viewModel.initWithState(arguments)

        setHasOptionsMenu(false)
    }

    override fun onResume() {
        super.onResume()
//        GoogleAnalytics.getInstance(requireContext()).report("/launcher")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val composeView = ComposeView(requireContext())

//        rootView.findViewById<View>(R.id.random_button).setOnClickListener { v -> viewModel.setRandomDate() }

        composeView.apply {
            setContent {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    DateTitleCompose(viewModel.current.date)
                    Divider(
                        color = colorResource(R.color.text),
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    CelebrationCompose(celebration = viewModel.current.celebration) {
                        viewModel.setRandomDate()
                    }
                }
            }
        }
        return composeView
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

@Composable
fun DateTitleCompose(
    date: MonthDay
) {
    Text(
        text = date.asTitle(),
        color =  colorResource(R.color.title),
        fontSize = 35.sp
    )
}

@Composable
fun CelebrationCompose(
    picasso: Picasso = PicassoHolder.get(),
    celebration: Celebration?,
    random: () -> Unit
) {
    Text(
        text = celebration?.name ?: stringResource(id = R.string.no_celebration),
        color = colorResource(R.color.text),
        modifier = Modifier.padding(top = 16.dp),
        textAlign = TextAlign.Center,
        fontSize = 24.sp
    )
    if (celebration == null) {
        Text(
            text = stringResource(R.string.random_day),
            color = colorResource(R.color.random),
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { random() },
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
        )
    }
    Image(
        painter = celebrationPainter(picasso, celebration),
        modifier = Modifier.padding(top = 16.dp),
        contentDescription = "Image"
    )
}

@Composable
fun MonthDay.asTitle(): String {
    return if (this.isEqual(MonthDay.now())) {
        stringResource(R.string.today)
    } else {
        val date = this.dayOfMonth
        val month = this.monthOfYear().getAsText(Locale.getDefault())
        stringResource(R.string.date_text_title, date, month)
    }
}

@Composable
fun celebrationPainter(
    picasso: Picasso = PicassoHolder.get(),
    celebration: Celebration?
): Painter {
    if (celebration == null) {
        return painterResource(R.drawable.vide)
    }
    return picasso.rememberPainter(key = celebration.path) {
        it.load(celebration.path).placeholder(R.drawable.noimage).error(R.drawable.noimage)
    }
}