package com.appers.ayvaz.thetravelingsalesman;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.appers.ayvaz.thetravelingsalesman.adapter.ClientAdapter;
import com.appers.ayvaz.thetravelingsalesman.adapter.ClientSearchAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>

 */
public class ClientListFragment extends Fragment {

    public static final int RANGE_RECENT = 0;
    public static final int RANGE_ALL = 1;
    public static final int RANGE_FAVORITE = 2;
    private static final String ARG_RANGE = "list_range";
    private int mRange = 1;
    private ClientAdapter mAdapter;
    private ClientSearchAdapter mSearchAdapter;
    private RecyclerView mRecyclerView;
    boolean mSearchOpen;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClientListFragment() {
    }

    // TODO: Customize parameter initialization
    public static ClientListFragment newInstance(int arg) {
        ClientListFragment fragment = new ClientListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RANGE, arg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mRange = getArguments().getInt(ARG_RANGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Set the adapter
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        Context context = view.getContext();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        updateUI();


        return view;
    }


    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {

        super.onResume();
        updateUI();
    }

    private void updateUI() {
        ClientManager clientContent = ClientManager.get(getActivity());
        List<Client> clients = clientContent.getClients(mRange);

        if (mSearchOpen && mSearchAdapter != null) {
            mRecyclerView.setAdapter(mSearchAdapter);
            return;
        }

        if (mAdapter == null) {
            mAdapter = new ClientAdapter(clients);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setClients(clients);
            mAdapter.notifyDataSetChanged();
            // following line necessary in the case of FragmentViewPager.. don't exactly know why
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_person:
                Intent intent = ClientEditActivity.newIntent(getActivity());
                startActivity(intent);
                return true;




            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mRange == RANGE_ALL) {
            inflater.inflate(R.menu.menu_client_list, menu);

//            SearchManager searchManager = (SearchManager) getActivity().
//                    getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//            // Assumes current activity is the searchable activity
//            searchView.setSearchableInfo(
//                    searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.requestFocusFromTouch();
            searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
            final MenuItem addPerson = menu.findItem(R.id.action_add_person);

            MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search),
                    new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    if (getActivity() instanceof LandingActivity) {
                        ((LandingActivity) getActivity()).hideTab();
                        addPerson.setVisible(false);
                        mSearchOpen = true;
                    }
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    if (getActivity() instanceof LandingActivity) {
                        ((LandingActivity) getActivity()).showTab();
                        addPerson.setVisible(true);
                        mRecyclerView.setAdapter(mAdapter);
                        mSearchOpen = false;
                    }
                    return true;
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    List<Client> result = ClientManager.get(getActivity()).getSearchResult(query);
                    mSearchAdapter = new ClientSearchAdapter(result);
                    mRecyclerView.setAdapter(mSearchAdapter);

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });


        }

    }


}
