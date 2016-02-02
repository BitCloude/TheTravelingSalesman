package com.appers.ayvaz.thetravelingsalesman;

import android.app.FragmentTransaction;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.database.TripCursorWrapper;
import com.appers.ayvaz.thetravelingsalesman.dialog.DatePickerFragment;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.models.Expense;
import com.appers.ayvaz.thetravelingsalesman.models.ExpenseContent;
import com.appers.ayvaz.thetravelingsalesman.models.Trip;
import com.appers.ayvaz.thetravelingsalesman.models.TripContent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//Intent Extras
//request expense tab(trip is default), KEY: ORIGIN, NAME: "EXPENSE"
//set default client, KEY: CLIENT, NAME: client's UUID in String

public class TripExpMan extends NavigationDrawerActivity implements SectionsPagerAdapter.PlaceholderFragment.SectionNumberHolder {


    LinearLayout linearLayout;
    RelativeLayout contextChildView = null;
    String[] from = {"October 2015", "November 2015", "December 2015", "January 2016", "February 2016", "March 2016", "April 2016", "May 2016", "June 2016", "July 2016", "August 2016", "September 2016"};

    String data = "Trip From Delhi to Hyderhabad";
    String cost = "$125,000";

    CharSequence mTitle, mDrawerTitle;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mDrawerToggle;
    int fragmentSection;
    static String clientDefault = null;



    @Override
    public void reportSection(int position) {

    }

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

        String request = null;

        Intent incoming = getIntent();
        if(incoming != null && incoming.hasExtra("ORIGIN"))
            request = incoming.getStringExtra("ORIGIN");
        if(incoming != null && incoming.hasExtra("CLIENT"))
            clientDefault = incoming.getStringExtra("CLIENT");


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
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                fragmentSection = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // mViewPager.setCurrentItem(tab.getPosition());

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        if(request !=null && request.equals("EXPENSE"))
        mViewPager.setCurrentItem(1); //expense tab

    }

    public void editIntent(Object obj){
        if(obj instanceof Trip) {
            Intent intent = new Intent(getApplicationContext(), TravelDetail.class);
            intent.putExtra("TRIP", (Trip) obj);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),""+((Trip) obj).getId(),Toast.LENGTH_LONG).show();
        }
        else if(obj instanceof Expense) {
            Intent intent = new Intent(getApplicationContext(), ExpenseAdd.class);
            intent.putExtra("EXPENSE", (Expense) obj);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),""+((Expense) obj).getId(),Toast.LENGTH_LONG).show();
        }

    }

    public void removeUtil(Object obj){

        TripContent tripContent = TripContent.get(getApplicationContext());
        ExpenseContent expenseContent = ExpenseContent.get(getApplicationContext());
        if(obj instanceof Trip) {
            tripContent.delete(((Trip) obj).getId());
            Toast.makeText(getApplicationContext(),""+((Trip) obj).getId(),Toast.LENGTH_LONG).show();
        }
        else if(obj instanceof Expense) {
            expenseContent.delete(((Expense) obj).getId());
            Toast.makeText(getApplicationContext(),""+((Expense) obj).getId(),Toast.LENGTH_LONG).show();
        }


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
        if (id == 66) {

            //SectionsPagerAdapter.PlaceholderFragment placeholderFragment = (SectionsPagerAdapter.PlaceholderFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentLinearLayout);


            if (fragmentSection == 0) {
                Intent intent = new Intent(getApplicationContext(), TravelDetail.class);
                if(SectionsPagerAdapter.PlaceholderFragment.selection !=null && !SectionsPagerAdapter.PlaceholderFragment.selection.getFirstName().equals("All"))
                    intent.putExtra("CLIENT", SectionsPagerAdapter.PlaceholderFragment.selection.getId().toString());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), ExpenseAdd.class);
                if(SectionsPagerAdapter.PlaceholderFragment.selection !=null && !SectionsPagerAdapter.PlaceholderFragment.selection.getFirstName().equals("All"))
                    intent.putExtra("CLIENT", SectionsPagerAdapter.PlaceholderFragment.selection.getId().toString());
                startActivity(intent);
            }

            return true;
        }

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
        contextChildView = (RelativeLayout) v;
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {


        int id = item.getItemId();

        switch (id) {

            case 11: {

                editIntent(contextChildView.getTag());
                Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
                return true;
            }

            case 22: {
                // SectionsPagerAdapter.PlaceholderFragment placeholderFragment = (SectionsPagerAdapter.PlaceholderFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentLinearLayout);
                // if(placeholderFragment != null){
                removeUtil(contextChildView.getTag());

                LinearLayout linear = (LinearLayout) contextChildView.getParent();
                linear.removeView(contextChildView);
                Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
                return true;
            }

             }
            return super.onContextItemSelected(item);



    }

    @Override
    protected void onResume() {
        super.onResume();
        checkMenu(R.id.nav_trip);
    }

}

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
     class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
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






        public static class PlaceholderFragment extends Fragment {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            SectionNumberHolder mCallback;


            static Client selection=null;
            AutoCompleteTextView autoCompleteTextView;
            TextView dateSet;
            ClientManager clientManager;
            List<Client> clientList;
            List<Object> list;
            boolean tripBool;
            ExpenseContent expenseContent;

            TripContent tripContent;

            public interface SectionNumberHolder {
                public void reportSection(int position);
            }

            LinearLayout linearLayout;
            RelativeLayout childLayout;
            String[] from = {"October 2015", "November 2015", "December 2015", "January 2016", "February 2016", "March 2016", "April 2016", "May 2016", "June 2016", "July 2016", "August 2016", "September 2016"};

            String data = "Trip From Delhi to Hyderhabad";
            String cost = "$125,000";

            String dataExp = "Hotel Bill From Delhi to Hyderhabad";
            String costExp = "$25,000";
            Calendar date = Calendar.getInstance();


            ImageButton button;
            private static final String ARG_SECTION_NUMBER = "section_number";
            private static int section;

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
                section = sectionNumber;
                return fragment;
            }


            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_trip_exp_man, container, false);
                tripBool = true;
                try {
                    mCallback = (SectionNumberHolder) getActivity();
                } catch (ClassCastException e) {
                    throw new ClassCastException(getActivity().toString()
                            + " must implement SectionNumberHolder");
                }

                dateSet = (TextView) rootView.findViewById(R.id.expensesDateText);
                button = (ImageButton) rootView.findViewById(R.id.fragmentCalenderButton);
                autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.tripExpManAutoClient);
                linearLayout = (LinearLayout) rootView.findViewById(R.id.fragmentLinearListLayout);
                //mCallback.reportSection(getArguments().getInt(ARG_SECTION_NUMBER));
                clientManager= ClientManager.get(getActivity());
                clientList = clientManager.getClients();
                Client clientAll = new Client();
                clientAll.setFirstName("All");
                clientAll.setLastName("");
                ArrayAdapter<Client> adapter = new ArrayAdapter<Client>(getActivity(), android.R.layout.simple_dropdown_item_1line, clientList);
                adapter.add(clientAll);
                autoCompleteTextView.setAdapter(adapter);
                expenseContent = ExpenseContent.get(getActivity());

                tripContent = TripContent.get(getActivity());

                if(TripExpMan.clientDefault != null && TripCursorWrapper.isUUIDValid(TripExpMan.clientDefault)) {
                    selection = clientManager.getClient(UUID.fromString(TripExpMan.clientDefault));
                    autoCompleteTextView.setText(selection.toString());

                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //DatePickerFragment datePickerFragmentragment = new DatePickerFragment();
                        // FragmentTransaction ft = getFragmentManager().beginTransaction;
                        // ft.add(YourFragment.newInstance(), null);
                        // ft.commit();
                        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(date);
                       datePickerFragment.setTargetFragment(PlaceholderFragment.this,0);
                        ft.add(datePickerFragment, null);
                        ft.commit();
                       // dateSet.setText(date.toString());

                    }
                });

                display();


                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selection= (Client)parent.getItemAtPosition(position);
                        linearLayout.removeAllViews();
                        display();
                    }
                });



                //END OF LOOP

                //  TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
           // format("%tD", cal);
            public void onActivityResult(int request_code, int resultCode, Intent intent){
                Bundle bundle = new Bundle();
                bundle = intent.getExtras();
                date = (Calendar) bundle.get("com.appers.avyaz.thetravelingsalesman.task.date");
                linearLayout.removeAllViews();
                display();

            }


            public void display(){

                dateSet.setText(String.format("%tm/%td/%tY", date,date,date));

                if(selection == null || selection.getFirstName().equals("All")){
                    if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                        list = new ArrayList<Object>(tripContent.getTrips()) ;
                        tripBool = true;
                    }
                    else {
                        list = new ArrayList<Object>(expenseContent.getExpenses());
                        tripBool = false;
                    }}
                else
                {
                    if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                        list = new ArrayList<Object>(tripContent.getClientTrips(selection.getId())) ;
                        tripBool = true;
                    }
                    else {
                        list = new ArrayList<Object>(expenseContent.getExpenses());
                        tripBool = false;
                    }


                }
                int i = 0;
                while(i < list.size()) {
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

                    if(tripBool) {
                        Trip trip = (Trip)list.get(i);
                        textdata.setText(trip.getClient_id().toString());
                        textcost.setText(trip.getTrip_to());
                        childLayout.setTag(trip);
                    }
                    else {
                        Expense expense = (Expense)list.get(i);
                        textdata.setText(expense.getDescription());
                        textcost.setText(expense.getAmount());
                        childLayout.setTag(expense);
                    }

                    /*if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                        textdata.setText(tripContent.getTrip(1).getTrip_from());
                        textcost.setText(tripContent.getTrip(1).getTrip_to());
                    } else {
                        textdata.setText(expenseContent.getExpense(1).getDescription());
                        textcost.setText(expenseContent.getExpense(1).getAmount());
                    }
                   */

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(2, 2, 2, 2);
                    childLayout.setLayoutParams(params);
                    childLayout.setBackgroundColor(Color.LTGRAY);
                    childLayout.addView(textdata, layoutParamsData);
                    childLayout.addView(textcost, layoutParamsCost);

                    getActivity().registerForContextMenu(childLayout);

                    TextView textView = new TextView(getActivity());
                    //textView.setText(from[i]);
                    //   ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,expenses);
                    // listViewExpenses.setAdapter(adapter4);

                    //childlayout.addView(textView);
                    //  linearLayout.addView(textView);
                    linearLayout.addView(childLayout);



                    i++;
                }


            }

        }


    }




/**
 * A placeholder fragment containing a simple view.
 */
