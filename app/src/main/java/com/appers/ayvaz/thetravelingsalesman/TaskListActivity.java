package com.appers.ayvaz.thetravelingsalesman;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.appers.ayvaz.thetravelingsalesman.adapter.TaskAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Task;
import com.appers.ayvaz.thetravelingsalesman.models.TaskManager;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;
import com.wefika.calendar.CollapseCalendarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskListActivity extends NavigationDrawerActivity {

    private final String TAG = "debuging--------";
    @Bind(R.id.calendar)
    CollapseCalendarView mCalendarView;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    //    private FragmentManager mFragmentManager;
//    private OnDateChanged mFragment;
    private TaskManager mTaskManager;
    private TaskAdapter mAdapter;
    private TaskAdapter mSearchAdapter;

    protected Fragment createFragment(LocalDate date) {
        return ClientTaskFragment.newInstance(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        ViewGroup appbar = (ViewGroup) findViewById(R.id.appbar);
        getLayoutInflater().inflate(R.layout.layout_collapse_calenar_view, appbar);
        ButterKnife.bind(this);

        setTitle(R.string.title_activity_task);

        mTaskManager = TaskManager.get(this);

//        CalendarManager manager = new CalendarManager(LocalDate.now(), CalendarManager.State.MONTH, LocalDate.now(), LocalDate.now().plusYears(1));
//        mCalendarView.init(manager);
        mCalendarView.init(LocalDate.now(), LocalDate.now().minusYears(3), LocalDate.now().plusYears(3));

        changeRange(LocalDate.now());

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


    }

    private void doMySearch(CharSequence query) {

        List<Task> result = TaskManager.get(this).search(query);
        if (mSearchAdapter == null) {
            mSearchAdapter = new TaskAdapter(result);
        } else {
            mSearchAdapter.setData(result);
            mSearchAdapter.notifyDataSetChanged();
        }

        mRecyclerView.setAdapter(mSearchAdapter);

    }

    private void changeRange(LocalDate localDate) {
        // when user click a date, change the adapter
//        Toast.makeText(getApplicationContext(), localDate.toString(), Toast.LENGTH_SHORT).show();
//        mFragment.updateUI(localDate);

        List<Task> result = mTaskManager.queryInstance(localDate);

        if (mAdapter == null) {
            mAdapter = new TaskAdapter(result);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(result);
            mAdapter.notifyDataSetChanged();

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMenu(R.id.nav_tasks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset_date:
                mCalendarView.getManager().selectDay(LocalDate.now());
                mCalendarView.populateLayout();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(
                SEARCH_SERVICE);

        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(
//                        this, TaskSearchResultActivity.class)));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setQueryHint(getString(R.string.search_hint_task));
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        if (mSearchAdapter == null) {
                            mSearchAdapter = new TaskAdapter(new ArrayList<Task>(0));
                        }
                        mRecyclerView.setAdapter(mSearchAdapter);
                        mCalendarView.setVisibility(View.GONE);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        mCalendarView.setVisibility(View.VISIBLE);
                        mRecyclerView.setAdapter(mAdapter);
                        return true;
                    }
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doMySearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    interface OnDateChanged {
        void updateUI(LocalDate date);
    }

    /*
    private void setUpWeekTable() {
        String[] namesOfDays = DateFormatSymbols.getInstance().getShortWeekdays();

        TableRow tableRow = (TableRow) findViewById(R.id.dateRow1);
        int cnt = tableRow.getChildCount();
        for (int i = 0; i < cnt; i++) {
            TextView tv = (TextView) tableRow.getChildAt(i);
            tv.setGravity(Gravity.CENTER);
            tv.setText(namesOfDays[i + 1]);
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.roll(Calendar.DATE, 0 - calendar.get(Calendar.DAY_OF_WEEK));

        tableRow = (TableRow) findViewById(R.id.dateRow2);
        for (int i = 0; i < tableRow.getChildCount(); i++) {
            TextView tv = (TextView) tableRow.getChildAt(i);
            tv.setGravity(Gravity.CENTER);
            tv.setText(""+calendar.get(Calendar.DATE));
            calendar.roll(Calendar.DATE, 1);
        }
    }
    */
}
