package com.simbiosyscorp.thetravelingsalesman.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simbiosyscorp.thetravelingsalesman.HelpActivity;
import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.utils.LoginUtils;

public abstract class NavigationDrawerActivity extends BaseActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout view_stub;
    TextView mUsername;
    SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        view_stub = (LinearLayout) findViewById(R.id.view_stub);

        mSharedPreferences = getSharedPreferences(LoginUtils.PREF_NAME, 0);

        // drawer stuff
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_menu);


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
//                item.setChecked(true);
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
//                setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(LoginUtils.PREF_NAME, 0);
        String username = sharedPreferences.getString(LoginUtils.KEY_USERNAME, "");
        mUsername = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.nav_username);
        mUsername.setText(username);

    }

    protected void checkMenu(int menuId) {
        if (menuId < 0) {
            return;
        }
        MenuItem currentItem = mNavigationView.getMenu().findItem(menuId);
        currentItem.setChecked(true);
    }


    private void selectItem(int itemId) {

        if (itemId == R.id.nav_reports) {
            startActivity(new Intent(this, ReportsActivity.class));
        } else if (itemId == R.id.nav_trip) {
            startActivity(new Intent(this, TripExpMan.class).putExtra("ORIGIN", "TRIP"));
        } else if (itemId == R.id.nav_expenses) {
            startActivity(new Intent(this, TripExpMan.class).putExtra("ORIGIN", "EXPENSE"));
        } else if (itemId == R.id.nav_tasks) {
            startActivity(new Intent(this, TaskListActivity.class));
        } else if (itemId == R.id.nav_reminders) {
            startActivity(new Intent(this, NotificationActivity.class));
        } else if (itemId == R.id.nav_clients) {
            startActivity(new Intent(this, LandingActivity.class));
        } else if (itemId == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (itemId == R.id.nav_exit) {
            finishAffinity();
        } else if (itemId == R.id.nav_help) {
            startActivity(new Intent(this, HelpActivity.class));
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

    /* Override all setContentView methods to put the content view to the FrameLayout view_stub
      * so that, we can make other activity implementations looks like normal activity subclasses.
      */


    @Override
    public void setContentView(int layoutResID) {
        if (view_stub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, view_stub, false);
            view_stub.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (view_stub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view_stub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view_stub != null) {
            view_stub.addView(view, params);
        }
    }



}
