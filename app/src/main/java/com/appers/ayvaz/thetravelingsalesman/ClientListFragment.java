package com.appers.ayvaz.thetravelingsalesman;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appers.ayvaz.thetravelingsalesman.adapter.ClientAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientContent;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>

 */
public class ClientListFragment extends Fragment {

    public static final int RECENT = 0;
    public static final int ALL = 1;
    public static final int FAVORITE = 2;
    private static final String ARG_RANGE = "list_range";
    private int mRange = -1;

    private ClientAdapter mAdapter;
    private RecyclerView mRecyclerView;

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
        ClientContent clientContent = ClientContent.get(getActivity());
        List<Client> clients = clientContent.getClients(mRange);

        if (mAdapter == null) {
            mAdapter = new ClientAdapter(clients);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setClients(clients);
            mAdapter.notifyDataSetChanged();
            // this line necessary in the case of FragmentViewPager.. don't exactly know why
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_client_list, menu);
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


}
