package com.appers.ayvaz.thetravelingsalesman;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ActionMode;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;


import com.appers.ayvaz.thetravelingsalesman.adapter.ClientAdapter;
import com.appers.ayvaz.thetravelingsalesman.adapter.ClientSearchAdapter;
import com.appers.ayvaz.thetravelingsalesman.dialog.DeleteAlertDialogFragment;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;

import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>

 */
public class ClientListFragment extends Fragment
        implements ActionMode.Callback,
        RecyclerView.OnItemTouchListener{

    private final String DEBUG_TAG = "ClientListFragment: ";
    public static final int RANGE_ALL = 0;
    public static final int RANGE_RECENT = 1;
    public static final int RANGE_FAVORITE = 2;
    private static final String ARG_RANGE = "list_range";
    private int mRange = 1;
    private ClientAdapter mAdapter;
    private ClientSearchAdapter mSearchAdapter;
    boolean mSearchOpen;
    private ActionMode actionMode;
    GestureDetectorCompat gestureDetector;

    private static final int REQUEST_DELETE = 3;
    private OnFragmentInteractionListener mListener;
    private TabLayout mTablayout;
    private AppBarLayout appBarLayout;
    private View mShadow;
    private MenuItem mEditClient;
    private TextView mActionTitle;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClientListFragment() {
    }

    interface OnFragmentInteractionListener {
        void updateFragments(int range);
    }

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

        ButterKnife.bind(this, view);

        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        mTablayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
        mShadow = getActivity().findViewById(R.id.appbar_shadow);

        // Set the adapter

        Context context = view.getContext();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(this);
        gestureDetector =
                new GestureDetectorCompat(getContext(), new ClientListGestureListener());

        updateUI();



        return view;
    }


    @Override
    public void onAttach(Context context) {
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }

        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onResume() {

        super.onResume();
        updateUI();
    }

    public void updateUI() {
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
            // fixed after setting ViewPager's offScreenLimit
//            mRecyclerView.setAdapter(mAdapter);
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

/** action mode */

    private void myToggleSelection(int idx) {
        mAdapter.toggleSelection(idx);
        int cnt = mAdapter.getSelectedItemCount();
        setActionTitle(cnt);
        Log.i(DEBUG_TAG, mAdapter.getSelectedItemCount() + " selected");
        mEditClient.setVisible(cnt == 1);


    }


    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        hideAppBar();
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_client_context, menu);

        actionMode.setCustomView(getActivity().getLayoutInflater()
                .inflate(R.layout.action_mode, null));

        mActionTitle = (TextView) actionMode.getCustomView().findViewById(R.id.textView);
        mEditClient = menu.findItem(R.id.action_edit);
        CheckBox checkBox = (CheckBox) actionMode.getCustomView().findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectAll();
                } else {
                    clearSelections();
                }
            }
        });






        return true;
    }

    private void selectAll() {
        mAdapter.selectAll();
        int cnt = mAdapter.getSelectedItemCount();
        setActionTitle(cnt);
        mEditClient.setVisible(cnt == 1);
    }

    private void clearSelections() {
        mAdapter.clearSelections();
        setActionTitle(0);
        mEditClient.setVisible(false);
    }

    private void setActionTitle(int cnt) {
        if (mActionTitle != null) {
            String title = getString(
                    R.string.selected_count,
                    cnt);
            //        mActionMode.setTitle(title);

            mActionTitle.setText(title);
        }
    }

    private void hideAppBar() {
//        appBarLayout.setVisibility(View.GONE);
        mTablayout.setVisibility(View.GONE);
        mShadow.setVisibility(View.GONE);

    }

    public void showAppBar() {
//        appBarLayout.setVisibility(View.VISIBLE);
        mTablayout.setVisibility(View.VISIBLE);
        mShadow.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_delete:
                int cnt = mAdapter.getSelectedItemCount();
                String s = cnt > 1 ? " client" : " clients";
                FragmentManager manager = getFragmentManager();
                DeleteAlertDialogFragment dialog = DeleteAlertDialogFragment.newInstance(cnt + s);
                dialog.setTargetFragment(ClientListFragment.this, REQUEST_DELETE);
                dialog.show(manager, null);
                return true;

            case R.id.action_edit:
                mAdapter.editSelected(getContext());
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        mAdapter.clearSelections();
        showAppBar();

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


    private class ClientListGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            int idx =  mRecyclerView.getChildAdapterPosition(view);
            if (idx < 0) {
                return super.onSingleTapConfirmed(e);
            }
            Log.i(DEBUG_TAG, "idx: " + idx);
            ClientAdapter.ViewHolder vh = (ClientAdapter.ViewHolder) mRecyclerView
                    .findViewHolderForAdapterPosition(idx);
            if (actionMode != null) {
                myToggleSelection(idx);
            } else {
                vh.onClick(view);
            }


            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (actionMode != null) {
                return;
            }
            // Start the CAB using the ActionMode.Callback defined above
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            actionMode = toolbar.startActionMode(
                    ClientListFragment.this);



            int idx = mRecyclerView.getChildAdapterPosition(view);
            myToggleSelection(idx);
            super.onLongPress(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_DELETE:

                List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
                for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
                    int currPos = selectedItemPositions.get(i);
                    mAdapter.removeData(currPos, getContext());
                    mAdapter.notifyItemChanged(currPos);
                }
                actionMode.finish();
                mListener.updateFragments(mRange);
        }

    }
}
