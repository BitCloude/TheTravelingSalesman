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
import android.widget.Button;

public class ReportsActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    CharSequence mTitle, mDrawerTitle;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        setTitle(R.string.reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        ActionBarDrawerToggle mDrawerToggle;
        // drawer stuff
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mTitle = mDrawerTitle = getTitle();
        //navi view implementation
        mNavigationView = (NavigationView) findViewById(R.id.navi_menu);
        setupDrawerContent(mNavigationView);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                selectItem(item.getItemId());
                mDrawerLayout.closeDrawers();
                return true;
            }
        });



        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void selectItem(int itemId) {
        if (itemId == R.id.nav_reports) {
            startActivity(new Intent(this, ReportsActivity.class));
        }
        if (itemId == R.id.nav_trip) {
            startActivity(new Intent(this, Travel.class));
        }


    }
    public void gotoExpenses(View view) {
        startActivity(new Intent(this, ReportsExpenseActivity.class));
    }

    public void gotoTasks(View view) {
        startActivity(new Intent(this, ReportsTasksActivity.class));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}
