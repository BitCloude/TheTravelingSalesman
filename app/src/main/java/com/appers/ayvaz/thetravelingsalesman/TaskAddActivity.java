package com.appers.ayvaz.thetravelingsalesman;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class TaskAddActivity extends AppCompatActivity
        implements TaskAddTime.OnFragmentInteractionListener,
        TaskAddInvite.OnFragmentInteractionListener,
        TaskAddLocation.OnFragmentInteractionListener,
        TaskAddNotes.OnFragmentInteractionListener {

    FragmentPagerAdapter mFragmentPagerAdapter;
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new TaskAddTime();
                }
                if (position == 1) {
                    return new TaskAddInvite();
                }
                if (position == 2) {
                    return new TaskAddLocation();
                }

                if (position == 3) {
                    return new TaskAddNotes();
                }

                return null;
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        mViewPager.setAdapter(mFragmentPagerAdapter);

        String[] tabTitles = getResources().getStringArray(R.array.tab_titles);
        // Add tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 4; i++) {
            tabLayout.addTab(
                    tabLayout.newTab()
                            .setText(tabTitles[i]));

        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_account_circle_black_24dp);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
