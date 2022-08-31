package packi.day.main

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squareup.picasso3.Picasso
import com.squareup.picasso3.compose.rememberPainter
import org.joda.time.MonthDay
import packi.day.di.DaggerFocusCelebrationComponent
import packi.day.image.PicassoHolder
import packi.day.store.Celebration
import packi.day.store.getStoreLocator
import java.util.*

private fun FocusCelebrationViewModelCompose(context: Context): FocusCelebrationViewModel {

    return DaggerFocusCelebrationComponent.builder()
        .storeLocator(context.getStoreLocator())
        .build().getViewModel()
}

@Composable
fun FocusCelebrationView(
    monthDay: MonthDay = MonthDay.now()
) {
    val context = LocalContext.current
    val viewModel = remember { FocusCelebrationViewModelCompose(context) }
    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    viewModel.updateState(monthDay)
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight()
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures( // TODO this doesn't work
                    onDragEnd = {
                        when {
                            offsetX.value >= 2 -> viewModel.plusDays(1)
                            offsetX.value <= 2 -> viewModel.plusDays(-1)
                            offsetY.value <= 2 -> viewModel.plusMonths(1)
                            offsetY.value >= 2 -> viewModel.plusMonths(1)
                        }
                    },
                    onDrag = { _, over ->
                        val original = Offset(offsetX.value, offsetY.value)
                        val summed = original + over
                        val newValue = Offset(
                            x = summed.x.coerceIn(0f, size.width - 50.dp.toPx()),
                            y = summed.y.coerceIn(0f, size.height - 50.dp.toPx())
                        )

                        offsetX.value = newValue.x
                        offsetY.value = newValue.y
                    }
                )

            },
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        DateTitleCompose(viewModel.current.date)
        Divider(
            color = colorResource(R.color.text),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 16.dp)
        )
        CelebrationCompose(celebration = viewModel.current.celebration) {
            viewModel.setRandomDate() // TODO this doesn't work
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