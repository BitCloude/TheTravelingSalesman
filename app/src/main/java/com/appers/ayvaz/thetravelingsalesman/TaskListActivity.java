package com.appers.ayvaz.thetravelingsalesman;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;


import com.appers.ayvaz.thetravelingsalesman.adapter.TaskAdapter;
import com.appers.ayvaz.thetravelingsalesman.models.Task;
import com.appers.ayvaz.thetravelingsalesman.models.TaskManager;
import com.appers.ayvaz.thetravelingsalesman.view.DividerItemDecoration;
import com.wefika.calendar.CollapseCalendarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskListActivity extends NavigationDrawerActivity
        implements ActionMode.Callback {

    private final String TAG = "debuging--------";
    @Bind(R.id.calendar)    CollapseCalendarView mCalendarView;
    @Bind(R.id.recycler_view)    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)    Toolbar mToolbar;
    @Bind(R.id.appbar_shadow) View mShadow;
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
        setContentView(R.layout.fragment_list);
        ViewGroup appbar = (ViewGroup) findViewById(R.id.appbar);
        getLayoutInflater().inflate(R.layout.layout_collapse_calenar_view, appbar);
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

        setUpActionMode();


        updateUI();


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
        updateUI();

    }



    private void updateUI() {

        if (mCalendarMode) {
            if (mActionMode == null) {
                mCalendarView.setVisibility(View.VISIBLE);
            } else {
                mCalendarView.setVisibility(View.GONE);
            }

            changeRange(mCalendarView.getSelectedDate());

        } else {
            mCalendarView.setVisibility(View.GONE);
            showAll();

        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset_date:
                selectToday();

                return true;
            case R.id.action_switch_view:
                MenuItem resetDate = mOptionsMenu.findItem(R.id.action_reset_date);
                MenuItem search = mOptionsMenu.findItem(R.id.action_search);
                MenuItem toggleMode = mOptionsMenu.findItem(R.id.action_switch_view);

                if (mCalendarMode) {
                    mCalendarView.setVisibility(View.GONE);
                    showAll();
                } else {
                    mCalendarView.setVisibility(View.VISIBLE);
                    changeRange(mCalendarView.getSelectedDate());
                }

                mCalendarMode = !mCalendarMode;
                resetDate.setVisible(mCalendarMode);
//                search.setVisible(!mCalendarMode);
                toggleMode.setIcon(mCalendarMode? listIcon : calendarIcon);

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAll() {
        Calendar begin = Calendar.getInstance();
        begin.set(2000, Calendar.JANUARY, 1);
        Calendar end = Calendar.getInstance();
        end.set(2030, Calendar.DECEMBER, 31);

        List<Task> result = TaskManager.get(this).queryInstance(begin, end, null);
        if (mAdapter == null) {
            mAdapter = new TaskAdapter(result);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(result);
            mAdapter.notifyDataSetChanged();
        }
    }

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
                            mSearchAdapter = new TaskAdapter(new ArrayList<Task>(0));
                        }
                        mRecyclerView.setAdapter(mSearchAdapter);
                        if (mCalendarMode) {
                            mCalendarView.setVisibility(View.GONE);
                        }
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        if (mCalendarMode) {
                            mCalendarView.setVisibility(View.VISIBLE);
                        }

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

        menu.findItem(R.id.action_reset_date).setVisible(mCalendarMode);
//        menu.findItem(R.id.action_search).setVisible(!mCalendarMode);
        menu.findItem(R.id.action_switch_view).setIcon(mCalendarMode ? listIcon : calendarIcon);

        return true;
    }

    /** action mode */

    private void setUpActionMode() {
        mGestureDetector =
                new GestureDetectorCompat(this, new TaskListGestureListener());

        mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return mGestureDetector.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };

        mRecyclerView.addOnItemTouchListener(mOnItemTouchListener);
    }

    private void myToggleSelection(int idx) {
        mAdapter.toggleSelection(idx);
        int cnt = mAdapter.getSelectedItemCount();
        setActionTitle(cnt);

        // hide button when multiple tasks are selected
        mChangeClient.setVisible(cnt == 1);
    }

    private void selectAll() {
        mAdapter.selectAll();
        int cnt = mAdapter.getSelectedItemCount();
        setActionTitle(cnt);
        mChangeClient.setVisible(cnt == 1);
    }

    private void clearSelections() {
        mAdapter.clearSelections();
        setActionTitle(0);
        mChangeClient.setVisible(false);
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


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_task_context, menu);
        mCalendarView.setVisibility(View.GONE);
        mode.setCustomView(getLayoutInflater().inflate(R.layout.action_mode, null));

        mActionTitle = (TextView) mode.getCustomView().findViewById(R.id.textView);
        mChangeClient = menu.findItem(R.id.action_change_client);
        CheckBox checkBox = (CheckBox) mode.getCustomView().findViewById(R.id.checkBox);
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

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                int cnt = mAdapter.getSelectedItemCount();
                String s = cnt > 1 ? " task" : " tasks";
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.r_u_sure, s))
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // todo:delete tasks
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null);

                builder.create().show();


                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
        mAdapter.clearSelections();
        mCalendarView.setVisibility(View.VISIBLE);
    }



    private class TaskListGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            int idx =  mRecyclerView.getChildAdapterPosition(view);
            if (idx < 0) {
                return super.onSingleTapConfirmed(e);
            }

            TaskAdapter.ViewHolder vh = (TaskAdapter.ViewHolder) mRecyclerView
                    .findViewHolderForAdapterPosition(idx);

            if (mActionMode != null) {
                myToggleSelection(idx);
            } else {
                vh.onClick(view);
            }


            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (mActionMode != null) {
                return;
            }
            // Start the CAB using the ActionMode.Callback defined above

            mActionMode = startSupportActionMode(TaskListActivity.this);

            int pos = mRecyclerView.getChildAdapterPosition(view);
//                Toast.makeText(TaskListActivity.this, "pos: " + pos, Toast.LENGTH_SHORT).show();
            myToggleSelection(pos);


            super.onLongPress(e);
        }
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
