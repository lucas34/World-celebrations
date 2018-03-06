package packi.day.ui.fragments

import android.app.Activity
import android.app.SearchManager
import android.app.SearchableInfo
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.analytics.GoogleAnalytics


import packi.day.R
import packi.day.common.report
import packi.day.ui.ActivityMain

class ListAllCelebrationFragment : Fragment(), SearchView.OnQueryTextListener {

    private var celebrationAdapter: Adapter? = null
    private var filter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        GoogleAnalytics.getInstance(context).report("/listAll")
        (activity as ActivityMain).setScreenTitle(R.string.list_all_celebrations)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("filter", filter)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(packi.day.R.layout.fragment_list_all_celebration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val realmRecyclerView = view.findViewById<RecyclerView>(R.id.realm_recycler_view)
        celebrationAdapter = Adapter(activity as ActivityMain?, getFilter(savedInstanceState))
        realmRecyclerView.layoutManager = GridLayoutManager(activity, computeNumberOfItems().toInt())
        realmRecyclerView.adapter = celebrationAdapter
    }

    private fun getFilter(savedInstanceState: Bundle?): String? {
        return if (savedInstanceState != null && savedInstanceState.containsKey("filter")) {
            savedInstanceState.getString("filter")
        } else null
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
        searchView.setQuery(filter, false)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        this.filter = newText
        celebrationAdapter!!.setFilter(newText)
        return true
    }
}
