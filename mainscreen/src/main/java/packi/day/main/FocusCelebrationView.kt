package packi.day.main

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso3.Picasso
import com.squareup.picasso3.compose.rememberPainter
import packi.day.di.DaggerAppComponent
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

        viewModel = ViewModelProvider(this, focusViewModelFactory)[FocusCelebrationViewModel::class.java]

        setHasOptionsMenu(false)
    }

    override fun onResume() {
        super.onResume()
//        GoogleAnalytics.getInstance(requireContext()).report("/launcher")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val composeView = ComposeView(requireContext())

//        rootView.findViewById<View>(R.id.fab).setOnClickListener { v -> viewModel.setRandomDate() }
//        rootView.findViewById<View>(R.id.random_button).setOnClickListener { v -> viewModel.setRandomDate() }

        composeView.apply {
            setContent {
                MessageCard(context, viewModel.observeCelebration())
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
fun MessageCard(
    context: Context,
    liveData: MutableState<InternationalDay>
) {
    val data by liveData
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = data.asTitle(),
            color =  colorResource(R.color.title),
            fontSize = 35.sp
        )
        Divider(
            color = colorResource(R.color.text),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = data.name,
            color =  colorResource(R.color.text),
            modifier = Modifier.padding(top = 16.dp),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
        Text(
            text = stringResource(R.string.random_day),
            color =  colorResource(R.color.text),
            modifier = Modifier.padding(top = 16.dp),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
        )
        Image(
            painter = makePainter(context, data),
            modifier = Modifier.padding(top = 16.dp),
            contentDescription = "Image"
        )
    }
}

@Composable
fun InternationalDay.asTitle(): String {
    return if (this.isToday) {
        stringResource(R.string.today)
    } else {
        val date = this.date.dayOfMonth
        val month = this.date.monthOfYear().getAsText(Locale.getDefault())
        stringResource(R.string.date_text_title, date, month)
    }
}

@Composable
fun makePainter(
    context: Context,
    data: InternationalDay
): Painter {
    val picasso = Picasso.Builder(context).build()
    return picasso.rememberPainter(key = data.drawable) {
        it.load(data.drawable).placeholder(R.drawable.noimage).error(R.drawable.noimage)
    }
}

//@Preview
//@Composable
//fun PreviewMessageCard() {
//    MessageCard(
//        msg = Message("Colleague", "Hey, take a look at Jetpack Compose, it's great!")
//    )
//}
