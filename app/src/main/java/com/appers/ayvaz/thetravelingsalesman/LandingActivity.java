package com.appers.ayvaz.thetravelingsalesman;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;

import com.appers.ayvaz.thetravelingsalesman.Model.Client;

public class LandingActivity extends NavigationDrawerActivity implements ClientListFragment.OnListFragmentInteractionListener {

    private ViewPager mViewPager;
    private String[] tabTitles;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private int[] rangeArg = {ClientListFragment.RECENT, ClientListFragment.ALL, ClientListFragment.FAVORITE};
//    private ClientListFragment[] mFragments = new ClientListFragment[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);


        ViewGroup appBar = (ViewGroup) findViewById(R.id.appbar);
        getLayoutInflater().inflate(R.layout.layout_tab, appBar);
        setTitle(R.string.title_activity_landing);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabTitles = getResources().getStringArray(R.array.tab_titles_landing);
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Log.i(".............", "getItem()");
                return ClientListFragment.newInstance(rangeArg[position]);
            }

            @Override
            public int getCount() {
                return tabTitles.length;
            }
        };

        mViewPager.setAdapter(mFragmentPagerAdapter);


        // Add tabs, specifying the tab's text and TabListener
        for (int i = 0; i < tabTitles.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabTitles[i]);
//            tab.setIcon(tabIcons[i]);
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
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client_list, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        //// TODO: 12/09/2015 search widget 
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        checkMenu(R.id.nav_clients);
    }

    @Override
    public void onListFragmentInteraction(Client item) {

    }
}
