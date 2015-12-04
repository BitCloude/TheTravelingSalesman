package com.appers.ayvaz.thetravelingsalesman;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ReportsActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewStub = (ViewGroup) findViewById(R.id.view_stub);
        getLayoutInflater().inflate(R.layout.activity_reports, viewStub);

        setTitle(R.string.reports);
    }


    public void gotoExpenses(View view) {
        startActivity(new Intent(this, ReportsExpenseActivity.class));
    }

    public void gotoTasks(View view) {
        startActivity(new Intent(this, ReportsTasksActivity.class));
    }

}
