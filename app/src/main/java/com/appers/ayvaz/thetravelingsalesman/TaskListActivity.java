package com.appers.ayvaz.thetravelingsalesman;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }

    private final String TAG = "debuging--------";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup appbar = (ViewGroup) findViewById(R.id.appbar);
        getLayoutInflater().inflate(R.layout.content_task, appbar);

        setTitle(R.string.title_activity_task);


        // set up week overview
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMenu(R.id.nav_tasks);
    }
}
