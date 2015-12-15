package com.appers.ayvaz.thetravelingsalesman;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.Model.Client;

public class LandingActivity extends BaseActivity implements ClientFragment.OnListFragmentInteractionListener{

    private ViewPager mViewPager;
    private String[] tabTitles;
    private FragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewStub = (ViewGroup) findViewById(R.id.view_stub);
        ViewGroup appBar = (ViewGroup) findViewById(R.id.appbar);

        getLayoutInflater().inflate(R.layout.activity_landing, viewStub);
        getLayoutInflater().inflate(R.layout.layout_tab, appBar);
        setTitle(R.string.title_activity_landing);
//        viewStub.removeView(findViewById(R.id.appbar_shadow));


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabTitles = getResources().getStringArray(R.array.tab_titles_landing);
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ClientFragment.newInstance(0);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_landing, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        //// TODO: 12/09/2015 search widget 
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public void onListFragmentInteraction(Client item) {
        Toast.makeText(getApplicationContext(), item.getFirstName()+item.getLastName(), Toast.LENGTH_LONG).show();
    }
}
