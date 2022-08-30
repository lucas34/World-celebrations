package packi.day.ui.fragments


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.analytics.GoogleAnalytics
import org.joda.time.MonthDay
import packi.day.R
import packi.day.common.report
import packi.day.main.celebrationPainter
import packi.day.store.Celebration
import packi.day.store.InternationalDay
import packi.day.store.StoreLocator
import packi.day.ui.ActivityMain
import java.util.*

class ListAllCelebrationsView : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: ListAllCelebrationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val locator = activity as StoreLocator?
                return ListAllCelebrationsViewModel(locator!!.store) as T
            }
        })[ListAllCelebrationsViewModel::class.java]

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        GoogleAnalytics.getInstance(requireContext()).report("/listAll")
        (activity as ActivityMain).setScreenTitle(R.string.list_all_celebrations)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                FooBar(requireContext(), viewModel.actual) {
                    val target = Intent(context, ActivityMain::class.java)
                    target.putExtra("date", it.date)
                    context.startActivity(target)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (activity == null) {
            super.onCreateOptionsMenu(menu, inflater)
        }

        val activity = activity ?: return

        inflater.inflate(R.menu.search_filter_all, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchableInfo = searchManager.getSearchableInfo(activity.componentName)

        searchView.setSearchableInfo(searchableInfo)

        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(this)
        searchView.setQuery(viewModel.filter, false)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.filter = newText
        return true
    }
}

@Composable
fun FooBar(
    context: Context,
    internationalDays: List<InternationalDay>,
    onItemClick: (InternationalDay) -> Unit
) {
    var currentMonth: MonthDay.Property? = null
    LazyVerticalGrid(
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
                CardCelebration(context, internationalDay.date, celebration, onItemClick)
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
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
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
    context: Context,
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