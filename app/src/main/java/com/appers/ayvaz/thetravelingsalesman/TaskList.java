package com.appers.ayvaz.thetravelingsalesman;

import android.os.Bundle;

import com.wefika.calendar.CollapseCalendarView;
import com.wefika.calendar.manager.CalendarManager;

import org.joda.time.LocalDate;

public class TaskList extends NavigationDrawerActivity {

    CollapseCalendarView mCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        CalendarManager manager = new CalendarManager(LocalDate.now(), CalendarManager.State.MONTH, LocalDate.now(),
                LocalDate.now().plusYears(1));

        mCalendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        mCalendarView.init(LocalDate.now(), LocalDate.now().minusMonths(1), LocalDate.now().plusYears(1));
    }
}
