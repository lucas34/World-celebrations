package packi.day.screen.list


import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.launch
import org.joda.time.MonthDay
import packi.day.R
import packi.day.main.celebrationPainter
import packi.day.store.Celebration
import packi.day.store.InternationalDay
import packi.day.store.StoreLocator
import java.util.*

private fun ListAllCelebrationsViewModelDagger(context: Context): ListAllCelebrationsViewModel {
    return ViewModelProvider(context as ViewModelStoreOwner, object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val locator = context as StoreLocator?
            return ListAllCelebrationsViewModel(locator!!.store) as T
        }
    })[ListAllCelebrationsViewModel::class.java]
}

@Composable
fun ListAllCelebrationsView(
    onItemClick: (InternationalDay) -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { ListAllCelebrationsViewModelDagger(context) }
    val filter = remember { mutableStateOf("") }
    val lazyGridState: LazyGridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        OutlinedTextField(
            value = filter.value,
            onValueChange = {
                filter.value = it
                coroutineScope.launch { lazyGridState.scrollToItem(0) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            label = { Text("Search") }
        )
        ListAllCelebrationsViewList(viewModel.getData(filter.value), lazyGridState, onItemClick)
    }
}

@Composable
fun ListAllCelebrationsViewList(
    internationalDays: List<InternationalDay>,
    state: LazyGridState,
    onItemClick: (InternationalDay) -> Unit
) {
    var currentMonth: MonthDay.Property? = null

    LazyVerticalGrid(
        state = state,
        columns = GridCells.Fixed(2)
    ) {
        for (internationalDay in internationalDays) {
            val celebration = internationalDay.celebration ?: continue
            val monthOfYear = internationalDay.date.monthOfYear()
            if (currentMonth != monthOfYear) {
                currentMonth = monthOfYear
                item {
                    HeaderText(monthOfYear.getAsText(Locale.getDefault()))
                }
            }
            item {
                CardCelebration(internationalDay.date, celebration, onItemClick)
            }
        }
    }
}


@Composable
fun HeaderText(
    title: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Text(
            text = title,
            color = colorResource(R.color.accent),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardCelebration(
    date: MonthDay,
    celebration: Celebration,
    onClick: (InternationalDay) -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier.padding(16.dp),
        onClick = {
            onClick(InternationalDay(date, celebration))
        }
    ) {
        Box(modifier = Modifier.height(100.dp)) {
            Image(
                painter = celebrationPainter(celebration = celebration),
                contentDescription = "Image",
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${date.dayOfMonth}")
                    }
                    append(" : " + celebration.name)
                },
                color = colorResource(R.color.white),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(R.color.text))
                    .align(Alignment.BottomCenter))
        }
    }
}