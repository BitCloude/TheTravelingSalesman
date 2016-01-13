package com.appers.ayvaz.thetravelingsalesman;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.models.TaskII;
import com.appers.ayvaz.thetravelingsalesman.models.TaskManager;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.utils.EventUtility;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientActivity extends AppCompatActivity {



    interface ClientChanged {
        void updateUI(Client client);
    }

    private FragmentPagerAdapter mFragmentPagerAdapter;

    private String[] tabTitles;
    private final int[] tabIcons = {};
    private long mEventId;

    private UUID mClientId;
    private Client mClient;
    private static final String EXTRA_CLIENT_ID = "client_id";

    private ClientChanged[] fragments;
    @Bind (R.id.editNewTask) EditText mEditNewTask;
    @Bind (R.id.newTaskOK)  ImageButton mNewTaskOk;
    @Bind (R.id.tabLayout) TabLayout mTabLayout;
    @Bind(R.id.viewpager) ViewPager mViewPager;
    @Bind(R.id.clientName)
    TextView mClientName;


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
        fragments = new ClientChanged[tabTitles.length];
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0: ClientTaskFragment taskFragment = ClientTaskFragment.newInstance(mClientId);
                        fragments[0] = taskFragment;
                        return taskFragment;

                    case 1: ClientCallLogFragment clientCallLogFragment = ClientCallLogFragment.newInstance(
                            mClient.getFirstPhone(), mClient.getSecondPhone());
                        fragments[1] = clientCallLogFragment;
                        return clientCallLogFragment;

                    case 2: ClientMessageFragment fragment = ClientMessageFragment
                            .newInstance(mClient.getFirstPhone(), mClient.getSecondPhone());
                        fragments[position] = fragment;
                        return fragment;
                    default: return new NotificationFragment();
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


    }

    private void createNewTask() {

        mEventId = EventUtility.getNewEventId(getApplicationContext().getContentResolver());

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
        // if prev_id == mEventId, means there is new events created
        // and we need to insert new events into local sqlite database.
        if (prev_id == mEventId) {
            // do database insert
            TaskII task = new TaskII(mEventId);
            task.setClient(mClient);
            TaskManager.get(getApplicationContext()).addTask(task);
        }

        updateUI();
    }

    private void updateUI() {
        if (mClientId == null) {
            finish();
        }
        mClient = ClientManager.get(getApplicationContext()).getClient(mClientId);
        if (mClient == null) {
            finish();
        }
        mClientName.setText(mClient.toString());
//        updateCallnText();

    }

    /**
    *  if the user edit the client info, reload call log and texts
    * */
    private void updateCallnText() {
        for (int i = 0; i < 3; i++) {
            if (fragments[i] != null) {
                fragments[i].updateUI(mClient);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_info) {
            Intent intent = ClientInfoActivity.newIntent(getApplicationContext(), mClientId);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * if user save the change
     *

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            updateUI();
            updateCallnText();
        }
    }

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





}
