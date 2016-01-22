package com.appers.ayvaz.thetravelingsalesman;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.models.Expense;
import com.appers.ayvaz.thetravelingsalesman.models.ExpenseContent;
import com.appers.ayvaz.thetravelingsalesman.models.Trip;
import com.appers.ayvaz.thetravelingsalesman.models.TripContent;

public class TripExpMan extends NavigationDrawerActivity {


    LinearLayout linearLayout;
    RelativeLayout childLayout;
    String[] from= {"October 2015", "November 2015", "December 2015","January 2016", "February 2016", "March 2016","April 2016", "May 2016", "June 2016", "July 2016", "August 2016", "September 2016"};

    String data = "Trip From Delhi to Hyderhabad";
    String cost = "$125,000";

    CharSequence mTitle, mDrawerTitle;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mDrawerToggle;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_exp_man);
//        ViewGroup viewStub = (ViewGroup) findViewById(R.id.view_stub);
//        getLayoutInflater().inflate(R.layout.activity_trip_exp_man, viewStub);
        ViewGroup appBar = (ViewGroup) findViewById(R.id.appbar);
        getLayoutInflater().inflate(R.layout.view_tab, appBar);

        setTitle("Trips and Expenses");
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
       setSupportActionBar(toolbar);
       ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
*/
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);



      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_trip_exp_man, menu);
        menu.add(1, 66, 100, "ADD");
        //menu.findItem(11).setIcon(android.R.drawable.ic_menu_save);
        menu.findItem(66).setIcon(R.drawable.ic_add_white_24dp);
        menu.findItem(66).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == 66)
            return true;
      /*  if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int groupId = 1;
        menu.add(groupId, 11, 100, "Edit");
        menu.add(groupId, 22, 200, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 11) {
            Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == 22) {
            Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onContextItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        LinearLayout linearLayout;
        RelativeLayout childLayout;
        String[] from= {"October 2015", "November 2015", "December 2015","January 2016", "February 2016", "March 2016","April 2016", "May 2016", "June 2016", "July 2016", "August 2016", "September 2016"};

        String data = "Trip From Delhi to Hyderhabad";
        String cost = "$125,000";

        String dataExp = "Hotel Bill From Delhi to Hyderhabad";
        String costExp = "$25,000";

        ImageButton button;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_trip_exp_man, container, false);


                linearLayout = (LinearLayout) rootView.findViewById(R.id.fragmentLinearLayout);

            Expense mExpense = new Expense(1);
                    mExpense.setDescription("Hotel bill from database");
                    mExpense.setAmount("50");
            Trip mTrip = new Trip(1);
                    mTrip.setTrip_from("from los Angeles");
                    mTrip.setTrip_to(" to NEW York");

            ExpenseContent expenseContent = ExpenseContent.get(getActivity());
            expenseContent.addExpense(mExpense);

            TripContent tripContent = TripContent.get(getActivity());
            tripContent.addTrip(mTrip);

                for (int i = 0; i < 12; i++) {
                    childLayout = new RelativeLayout(getActivity());
                    RelativeLayout.LayoutParams layoutParamsCost = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParamsCost.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    RelativeLayout.LayoutParams layoutParamsData = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParamsData.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    //childLayout.setOrientation(LinearLayout.HORIZONTAL);
                    TextView textdata = new TextView(getActivity());
                    textdata.setLines(2);
                    TextView textcost = new TextView(getActivity());
                    textcost.setLines(1);

                    if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                        textdata.setText(tripContent.getTrip(1).getTrip_from());
                        textcost.setText(tripContent.getTrip(1).getTrip_to());
                    }
                    else {
                        textdata.setText(expenseContent.getExpense(1).getDescription());
                        textcost.setText(expenseContent.getExpense(1).getAmount());
                    }


                    childLayout.setBackgroundColor(Color.LTGRAY);
                    childLayout.addView(textdata, layoutParamsData);
                    childLayout.addView(textcost, layoutParamsCost);


                    TextView textView = new TextView(getActivity());
                    textView.setText(from[i]);
                    //   ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,expenses);
                    // listViewExpenses.setAdapter(adapter4);

                    //childlayout.addView(textView);
                    linearLayout.addView(textView);
                    linearLayout.addView(childLayout);

                    registerForContextMenu(childLayout);

                   button = (ImageButton) rootView.findViewById(R.id.fragmentCalenderButton);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(),ReportsActivity.class);
                            startActivity(intent);
                        }
                    });

                }




          //  TextView textView = (TextView) rootView.findViewById(R.id.section_label);
           // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getClient is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Trip";
                case 1:
                    return "Expenses";
            }
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMenu(R.id.nav_trip);
    }
}
