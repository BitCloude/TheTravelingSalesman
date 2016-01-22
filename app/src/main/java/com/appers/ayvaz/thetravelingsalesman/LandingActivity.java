package com.appers.ayvaz.thetravelingsalesman;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class LandingActivity extends NavigationDrawerActivity
        implements ClientListFragment.OnFragmentInteractionListener{

    private ViewPager mViewPager;
    private String[] tabTitles;
    private TabLayout mTablayout;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private int[] rangeArg = {
            ClientListFragment.RANGE_ALL,
            ClientListFragment.RANGE_RECENT,
            ClientListFragment.RANGE_FAVORITE};
    private ClientListFragment[] mFragments = new ClientListFragment[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);


        ViewGroup container = (ViewGroup) findViewById(R.id.tabContainer);
        View viewTab = getLayoutInflater().inflate(R.layout.view_tab, container);


        setTitle(R.string.title_activity_landing);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTablayout = (TabLayout) findViewById(R.id.tabLayout);
        tabTitles = getResources().getStringArray(R.array.tab_titles_landing);
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
//                Log.i(".............", "getItem()");
                mFragments[position] = ClientListFragment.newInstance(rangeArg[position]);
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return tabTitles.length;
            }
        };

        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        // Add tabs, specifying the tab's text and TabListener
        for (int i = 0; i < tabTitles.length; i++) {
            TabLayout.Tab tab = mTablayout.newTab();
            tab.setText(tabTitles[i]);
//            tab.setIcon(tabIcons[i]);
            mTablayout.addTab(tab);
        }

        mTablayout.setTabGravity(TabLayout.GRAVITY_FILL);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    protected void onResume() {
        super.onResume();
        checkMenu(R.id.nav_clients);
    }

    public void hideTab() {
        if (mTablayout != null) {
            mTablayout.setVisibility(View.GONE);
        }
    }

    public void showTab() {
        if (mTablayout != null) {
            mTablayout.setVisibility(View.VISIBLE);
        }
    }


    public void updateFragments(int caller) {
        for (int i = 0; i < tabTitles.length; i++) {
            if (i == caller || mFragments[i] == null) {
                continue;
            }

            mFragments[i].updateUI();
        }
    }


}
