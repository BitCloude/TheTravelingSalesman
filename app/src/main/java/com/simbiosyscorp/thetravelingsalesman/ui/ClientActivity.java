package com.simbiosyscorp.thetravelingsalesman.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.models.Client;
import com.simbiosyscorp.thetravelingsalesman.models.ClientManager;
import com.simbiosyscorp.thetravelingsalesman.models.Task;
import com.simbiosyscorp.thetravelingsalesman.models.TaskManager;
import com.simbiosyscorp.thetravelingsalesman.utils.EventUtility;

import java.util.Calendar;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientActivity extends BaseActivity {


    private static final String DEBUG_TAG = "ClientActivity: ";
    private static final String EXTRA_CLIENT_ID = "client_id";
    private final int[] tabIcons = {};
    @Bind(R.id.editNewTask)
    EditText mEditNewTask;
    @Bind(R.id.newTaskOK)
    ImageButton mNewTaskOk;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private String[] tabTitles;
    private long mEventId;
    private UUID mClientId;
    private Client mClient;
    private boolean mNewTaskClicked = false;
    private ClientChanged[] fragments;

    public static Intent newIntent(Context packageContext, UUID clientId) {
        Intent i = new Intent(packageContext, ClientActivity.class);
        i.putExtra(EXTRA_CLIENT_ID, clientId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ViewGroup appbar = (ViewGroup) findViewById(R.id.appbar);
//        View view = getLayoutInflater().inflate(R.layout.view_client_header, appbar);

        ButterKnife.bind(this);

        setTitle(R.string.title_activity_client);
        tabTitles = getResources().getStringArray(R.array.tab_titles_client);

        mClientId = (UUID) getIntent().getSerializableExtra(EXTRA_CLIENT_ID);
        if (mClientId == null) {
            finish();
        }

        fragments = new ClientChanged[tabTitles.length];
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        ClientTaskFragment taskFragment = ClientTaskFragment.newInstance(mClientId);
                        fragments[0] = taskFragment;
                        return taskFragment;

                    case 1:
                        ClientCallLogFragment clientCallLogFragment = ClientCallLogFragment.newInstance(
                                mClient.getFirstPhone(), mClient.getSecondPhone());
                        fragments[1] = clientCallLogFragment;
                        return clientCallLogFragment;

                    case 2:
                        ClientMessageFragment fragment = ClientMessageFragment
                                .newInstance(mClient.getFirstPhone(), mClient.getSecondPhone());
                        fragments[2] = fragment;
                        return fragment;

                    default:
                        return new NotificationFragment();
                }

            }

            @Override
            public int getCount() {
                return tabTitles.length;
            }
        };
        updateUI();
        mViewPager.setAdapter(mFragmentPagerAdapter);

        // Add tabs, specifying the tab's text and TabListener
        for (int i = 0; i < tabTitles.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText(tabTitles[i]);
            mTabLayout.addTab(tab);
        }

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mViewPager.setOffscreenPageLimit(tabTitles.length);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        mNewTaskOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNewTask();
            }
        });

        new ClearEvents().execute();


    }

    private void insertEvent() {
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.MINUTE, 30);
        startMillis = beginTime.getTimeInMillis();
        endMillis = endTime.getTimeInMillis();


        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, mEditNewTask.getText().toString());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        // todo: timezone

//        values.put(CalendarContract.Events.EVENT_TIMEZONE, );
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        mEventId = Long.parseLong(uri.getLastPathSegment());
    }
    private void createNewTask() {

        // todo: change the way of adding new tasks 2.13.2016

        mNewTaskClicked = true;


        mEventId = EventUtility.getNewEventId(this.getContentResolver());
        Log.i(DEBUG_TAG, "mEventID: " + mEventId);

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, mEditNewTask.getText().toString())
                .putExtra(Intent.EXTRA_EMAIL, mClient.getEmail());
        startActivity(intent);


    }

    @Override
    protected void onResume() {
        super.onResume();


        long prev_id = EventUtility.getLastEventId(getContentResolver());

        Log.i(DEBUG_TAG, "prev_id: " + prev_id);

//         if prev_id == mEventId, means there is new events created
//         and we need to insert new events into local sqlite database.
        if (mEventId > 0 && prev_id >= mEventId) {
            // do database insert
            Log.i("Task", "inserting");
            Task task = new Task(mEventId);
            task.setClient(mClient);
            TaskManager.get(this).addTask(task);
            mEventId = -1;
            mEditNewTask.setText("");
        }

        updateUI();

    }

    private void updateUI() {
        mClient = ClientManager.get(this).getClient(mClientId);
        if (mClient == null) {
            finish();
            return;
        }

        setTitle(mClient.toString());
        updateFragments();

    }

    /**
     * if the user edit the client info, reload call log and texts
     */
    public void updateFragments() {
        for (int i = 0; i < tabTitles.length; i++) {
            if (fragments[i] != null) {
                fragments[i].updateUI(mClient);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

        case R.id.action_info:
                intent = ClientInfoActivity.newIntent(this, mClientId);
                startActivity(intent);
            return true;

        case R.id.action_trip:
                intent = TripExpMan.newTripIntent(this, mClientId);
                startActivity(intent);
                return true;
        case R.id.action_expense:
                intent = TripExpMan.newExpenseIntent(this, mClientId);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * if user save the change
     *
     * @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     * if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
     * updateUI();
     * updateCallnText();
     * }
     * }
     */


    public Client getClient() {
        return mClient;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }


    interface ClientChanged {
        void updateUI(Client client);
    }

    private class ClearEvents extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            TaskManager.get(getApplicationContext()).clearTask();
            return null;
        }
    }




}
