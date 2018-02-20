package packi.day.ui.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import packi.day.R;
import packi.day.common.AnalyticsTracker;
import packi.day.ui.ActivityMain;

public class ListAllCelebrationFragment extends Fragment implements SearchView.OnQueryTextListener {

    private Adapter celebrationAdapter;
    private @Nullable String filter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsTracker.getInstance(getActivity()).sendTracker("/listAll");
        ((ActivityMain) getActivity()).setScreenTitle(R.string.list_all_celebrations);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("filter", filter);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(packi.day.R.layout.fragment_list_all_celebration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView realmRecyclerView = view.findViewById(R.id.realm_recycler_view);
        celebrationAdapter = new Adapter((ActivityMain) getActivity(), getFilter(savedInstanceState));
        realmRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), (int) computeNumberOfItems()));
        realmRecyclerView.setAdapter(celebrationAdapter);
    }

    private @Nullable String getFilter(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("filter")) {
            return savedInstanceState.getString("filter");
        }
        return null;
    }

    private double computeNumberOfItems() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int round = Math.round(dpWidth / 200);
        return round < 1 ? 1 : round;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(packi.day.R.menu.search_filter_all, menu);

        MenuItem searchItem = menu.findItem(packi.day.R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        Activity activity = getActivity();
        if (activity == null) {
            super.onCreateOptionsMenu(menu, inflater);
            return;
        }

        SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchableInfo searchableInfo = searchManager.getSearchableInfo(activity.getComponentName());
            searchView.setSearchableInfo(searchableInfo);
        }

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQuery(filter, false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.filter = newText;
        celebrationAdapter.setFilter(newText);
        return true;
    }
}
