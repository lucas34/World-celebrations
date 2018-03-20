package packi.day.ui.fragments

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import com.google.android.gms.analytics.GoogleAnalytics
import packi.day.R
import packi.day.common.report
import packi.day.store.CelebrationComponent
import packi.day.ui.ActivityMain

class ListAllCelebrationsView : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: ListAllCelebrationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListAllCelebrationsViewModel::class.java)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        GoogleAnalytics.getInstance(context).report("/listAll")
        (activity as ActivityMain).setScreenTitle(R.string.list_all_celebrations)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(packi.day.R.layout.fragment_list_all_celebration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListAllCelebrationsAdapter(this)

        viewModel.observeList().observe(this, Observer<List<CelebrationComponent>> { setListData ->
            if (setListData != null) {
                adapter.setData(setListData)
            }
        })

        val realmRecyclerView = view.findViewById<RecyclerView>(R.id.realm_recycler_view)

        realmRecyclerView.layoutManager = GridLayoutManager(activity, computeNumberOfItems().toInt())
        realmRecyclerView.adapter = adapter
    }

    private fun computeNumberOfItems(): Double {
        val displayMetrics = resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        val round = Math.round(dpWidth / 200)
        return (if (round < 1) 1 else round).toDouble()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (activity == null) {
            super.onCreateOptionsMenu(menu, inflater)
        }

        val activity = activity ?: return

        inflater.inflate(packi.day.R.menu.search_filter_all, menu)

        val searchItem = menu.findItem(packi.day.R.id.action_search)
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
