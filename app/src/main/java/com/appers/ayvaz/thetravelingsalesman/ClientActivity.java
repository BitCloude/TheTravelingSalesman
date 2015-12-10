package com.appers.ayvaz.thetravelingsalesman;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;

public class ClientActivity extends BaseActivity {

    private FragmentPagerAdapter mFragmentPagerAdapter;
    private ViewPager mViewPager;
    private String[] tabTitles;
    private final int[] tabIcons = {R.drawable.ic_account_circle_black_24dp,
            R.drawable.ic_account_circle_black_24dp,
            R.drawable.ic_account_circle_black_24dp,
            R.drawable.ic_account_circle_black_24dp};
    private int fakeClientId = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_client);
        ViewGroup vg = (ViewGroup) findViewById(R.id.view_stub);
        getLayoutInflater().inflate(R.layout.activity_client, vg);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabTitles = getResources().getStringArray(R.array.tab_titles_client);
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
//                    return TaskFragment.newInstance(fakeClientId);
                }

                return null;
            }

            @Override
            public int getCount() {
                return tabTitles.length;
            }
        };

        mViewPager.setAdapter(mFragmentPagerAdapter);


        // Add tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 4; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabTitles[i]);
            tab.setIcon(tabIcons[i]);
            tabLayout.addTab(tab);
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


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


    public void onFragmentInteraction(Uri uri) {

    }
}
