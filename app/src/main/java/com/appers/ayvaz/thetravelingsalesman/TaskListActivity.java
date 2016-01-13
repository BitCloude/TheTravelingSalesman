package com.appers.ayvaz.thetravelingsalesman;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wefika.calendar.CollapseCalendarView;

import org.joda.time.LocalDate;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TaskListActivity extends NavigationDrawerActivity {

    interface OnDateChanged {
        void updateUI(LocalDate date);
    }

    private final String TAG = "debuging--------";
    @Bind(R.id.calendar)
    CollapseCalendarView mCalendarView;
    private FragmentManager mFragmentManager;
    private OnDateChanged mFragment;


    protected Fragment createFragment(LocalDate date) {
        return ClientTaskFragment.newInstance(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        ViewGroup appbar = (ViewGroup) findViewById(R.id.appbar);
        getLayoutInflater().inflate(R.layout.layout_collapse_calenar_view, appbar);
        ButterKnife.bind(this);

        setTitle(R.string.title_activity_task);

//        CalendarManager manager = new CalendarManager(LocalDate.now(), CalendarManager.State.MONTH, LocalDate.now(), LocalDate.now().plusYears(1));
//        mCalendarView.init(manager);
        mCalendarView.init(LocalDate.now(), LocalDate.now().minusYears(3), LocalDate.now().plusYears(3));


        mCalendarView.setListener(new CollapseCalendarView.OnDateSelect() {
            @Override
            public void onDateSelected(LocalDate localDate) {
                changeRange(localDate);
            }
        });

        mFragmentManager = getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment(mCalendarView.getSelectedDate());
            mFragment = (OnDateChanged) fragment;
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }


    private void changeRange(LocalDate localDate) {
        // when user click a date, change the adapter
        Toast.makeText(getApplicationContext(), localDate.toString(), Toast.LENGTH_SHORT).show();
        mFragment.updateUI(localDate);
//        Fragment fragment = createFragment(localDate);
//        mFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
//                .commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMenu(R.id.nav_tasks);
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
