package com.appers.ayvaz.thetravelingsalesman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.appers.ayvaz.thetravelingsalesman.Adapters.TaskAdapter;
import com.appers.ayvaz.thetravelingsalesman.dummy.DummyContent;

import java.util.Calendar;

public class TaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewStub = (ViewGroup) findViewById(R.id.view_stub);
        getLayoutInflater().inflate(R.layout.activity_task, viewStub);
        setTitle(R.string.title_activity_task);

        CalendarView cv = (CalendarView) findViewById(R.id.calendarView2);
        Calendar calendar = Calendar.getInstance();
        int curr = calendar.get(Calendar.DAY_OF_WEEK);
        cv.setMinDate(calendar.get(Calendar.DATE)- curr);

        //set up RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new TaskAdapter(DummyContent.TASKS));


    }
}
