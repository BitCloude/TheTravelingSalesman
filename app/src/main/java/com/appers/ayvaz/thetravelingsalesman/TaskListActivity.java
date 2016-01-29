package com.appers.ayvaz.thetravelingsalesman;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.appers.ayvaz.thetravelingsalesman.adapter.TaskAdapter;
import com.appers.ayvaz.thetravelingsalesman.adapter.TaskReportAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.models.Task;
import com.appers.ayvaz.thetravelingsalesman.models.TaskManager;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;
import com.wefika.calendar.CollapseCalendarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskListActivity extends NavigationDrawerActivity {

    private final String DEBUG_TAG = "TaskListActivity: ";
    private final int REQUEST_CLIENT = 0;

    @Bind(R.id.calendar)    CollapseCalendarView mCalendarView;
    @Bind(R.id.recyclerView)    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)    Toolbar mToolbar;
    @Bind(R.id.appbar_shadow) View mShadow;
    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;
    @Bind(R.id.progressBarFrame)
    FrameLayout mProgressBarContainer;
    //    private FragmentManager mFragmentManager;
//    private OnDateChanged mFragment;
    private TaskManager mTaskManager;
    private TaskAdapter mAdapter;
    private TaskAdapter mSearchAdapter;
    private ActionMode mActionMode;
    private GestureDetectorCompat mGestureDetector;
    private RecyclerView.OnItemTouchListener mOnItemTouchListener;
    private Menu mOptionsMenu;
    private boolean mCalendarMode = true;
    private TextView mActionTitle;
    private MenuItem mChangeClient;
    private ArrayAdapter<Task> arrayAdapter;


    private final int calendarIcon = R.drawable.ic_date_range_white_24dp;
    private final int listIcon = calendarIcon; // R.drawable.ic_list_white_18dp;

    protected Fragment createFragment(LocalDate date) {
        return ClientTaskFragment.newInstance(date);
    }

    interface OnDateChanged {
        void updateUI(LocalDate date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_list);
        setContentView(R.layout.activity_task_list);
        ButterKnife.bind(this);

        setTitle(R.string.title_activity_task);

        mTaskManager = TaskManager.get(this);

//        CalendarManager manager = new CalendarManager(LocalDate.now(), CalendarManager.State.MONTH, LocalDate.now(), LocalDate.now().plusYears(1));
//        mCalendarView.init(manager);
        mCalendarView.init(LocalDate.now(), LocalDate.now().minusYears(3), LocalDate.now().plusYears(3));


        mCalendarView.setListener(new CollapseCalendarView.OnDateSelect() {
            @Override
            public void onDateSelected(LocalDate localDate) {
                changeRange(localDate);
            }
        });

        /*mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment(mCalendarView.getSelectedDate());
            mFragment = (OnDateChanged) fragment;
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }*/

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);

        updateUI();
        registerForContextMenu(mRecyclerView);
//        setUpActionMode();




    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:
                alertDeletion();
                return true;
            case R.id.action_change_client:
                startActivityForResult(new Intent(this, ClientPickActivity.class),
                        REQUEST_CLIENT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void alertDeletion() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.r_u_sure, "Task"))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = mAdapter.deleteSelected();
                        Toast.makeText(TaskListActivity.this, getString(R.string.task_delete_result,
                                result ? "" : "not"), Toast.LENGTH_SHORT)
                                .show();
                        if (result) {
                            updateUI();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.show();

    }

    private void doMySearch(CharSequence query) {

        List<Task> result = TaskManager.get(this).search(query);

        if (mSearchAdapter == null) {
            mSearchAdapter = new TaskAdapter(result, this);
            mRecyclerView.setAdapter(mSearchAdapter);
        } else {
            mSearchAdapter.setData(result);
            mSearchAdapter.notifyDataSetChanged();
        }



    }

    private void changeRange(LocalDate date) {

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth(), 0, 0);
        Calendar endTime = Calendar.getInstance();
        endTime.set(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth(), 23, 59);

        new GetTask().execute(beginTime, endTime);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMenu(R.id.nav_tasks);
        updateUI();

    }



    private void updateUI() {

            if (mActionMode == null) {
                mCalendarView.setVisibility(View.VISIBLE);
            } else {
                mCalendarView.setVisibility(View.GONE);
            }

            changeRange(mCalendarView.getSelectedDate());

    }

    private class GetTask extends AsyncTask<Calendar, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(Calendar... params) {

            return TaskManager.get(TaskListActivity.this)
                    .queryInstance(params[0], params[1], null);
        }

        @Override
        protected void onPreExecute() {
            mProgressBarContainer.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }


        @Override
        protected void onPostExecute(List<Task> tasks) {
            if (mAdapter == null) {
                mAdapter = new TaskAdapter(tasks, TaskListActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setData(tasks);
                mAdapter.notifyDataSetChanged();
            }

            mAdapter.setShowYear(!mCalendarMode);

            mProgressBarContainer.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        }
    }




    private void showAll() {
        Calendar begin = Calendar.getInstance();
        begin.set(2000, Calendar.JANUARY, 1);
        Calendar end = Calendar.getInstance();
        end.set(2030, Calendar.DECEMBER, 31);

        new GetTask().execute(begin, end, null);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset_date:
                selectToday();

                return true;
           /* case R.id.action_switch_view:
                MenuItem resetDate = mOptionsMenu.findItem(R.id.action_reset_date);
                MenuItem search = mOptionsMenu.findItem(R.id.action_search);
                MenuItem toggleMode = mOptionsMenu.findItem(R.id.action_switch_view);


                mCalendarMode = !mCalendarMode;
                updateUI();
                resetDate.setVisible(mCalendarMode);
                toggleMode.setIcon(mCalendarMode ? listIcon : calendarIcon);

                return true;
*/



            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }
//
//        if (requestCode == REQUEST_CLIENT) {
//            UUID id = UUID.fromString(data.getStringExtra(ClientPickActivity.EXTRA_CLIENT_ID));
//            Client client = ClientManager.get(this).getClient(id);
//            boolean s = mAdapter.setClient(client, this);
//            Log.i("updated successfully: ", s+"");
//        }
//    }

    private void selectToday() {
        mCalendarView.getManager().selectDay(LocalDate.now());
        mCalendarView.populateLayout();
        changeRange(LocalDate.now());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks, menu);

        mOptionsMenu = menu;

        /** search view **/
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
// Get the SearchView and set the searchable configuration
//        SearchManager searchManager = (SearchManager) getSystemService(
//                SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(
//                        this, TaskSearchResultActivity.class)));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setQueryHint(getString(R.string.search_hint_task));
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        if (mSearchAdapter == null) {
                            mSearchAdapter = new TaskAdapter(new ArrayList<Task>(0),
                                    TaskListActivity.this);
                        }

                        mRecyclerView.setAdapter(mSearchAdapter);
                        if (mCalendarMode) {
                            mCalendarView.setVisibility(View.GONE);
                        }

                        mOptionsMenu.setGroupVisible(R.id.nonSearchAction, false);

                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        if (mCalendarMode) {
                            mCalendarView.setVisibility(View.VISIBLE);
                        }

                        mSearchAdapter.clearData();
                        mRecyclerView.setAdapter(mAdapter);
                        mOptionsMenu.setGroupVisible(R.id.nonSearchAction, true);
                        return true;
                    }
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(DEBUG_TAG, newText);
                doMySearch(newText);
                return true;
            }
        });

        menu.findItem(R.id.action_reset_date).setVisible(mCalendarMode);

        return true;
    }










}
