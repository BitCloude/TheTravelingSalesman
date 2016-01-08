package com.appers.ayvaz.thetravelingsalesman;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.dialog.DeleteAlertDialogFragment;
import com.appers.ayvaz.thetravelingsalesman.Model.Client;
import com.appers.ayvaz.thetravelingsalesman.Model.ClientContent;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientActivity extends NavigationDrawerActivity implements DeleteAlertDialogFragment
        .NoticeDialogListener{



    interface ClientChanged {
        void updateUI(Client client);
    }

    private FragmentPagerAdapter mFragmentPagerAdapter;
    private MenuItem mStar;
    private String[] tabTitles;
    private final int[] tabIcons = {};
    private int fakeClientId = 12345;
    private UUID mClientId;
    private Client mClient;
    private static final String EXTRA_CLIENT_ID = "client_id";
    private static final String DIALOG_DELETE = "DialogDelete";
    private final int REQUEST_DELETE = 2;
    private final int REQUEST_EDIT = 0;
    private ClientChanged[] fragments;
    @Bind (R.id.editNewTask) EditText mEditNewTask;
    @Bind (R.id.newTaskOK)  ImageButton mNewTaskOk;
    @Bind (R.id.tabLayout) TabLayout mTabLayout;
    @Bind(R.id.viewpager) ViewPager mViewPager;

    public static Intent newIntent(Context packageContext, UUID clientId) {
        Intent i = new Intent(packageContext, ClientActivity.class);
        i.putExtra(EXTRA_CLIENT_ID, clientId);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        ViewGroup appbar = (ViewGroup) findViewById(R.id.appbar);
        View view = getLayoutInflater().inflate(R.layout.activity_client_header, appbar);
        ButterKnife.bind(this);
        tabTitles = getResources().getStringArray(R.array.tab_titles_client);

        mClientId = (UUID) getIntent().getSerializableExtra(EXTRA_CLIENT_ID);
        fragments = new ClientChanged[tabTitles.length];
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
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
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, mEditNewTask.getText().toString())
                .putExtra(Intent.EXTRA_EMAIL, mClient.getEmail());
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        mClient = ClientContent.get(getApplicationContext()).getClient(mClientId);
        if (mClient == null) {
            finish();
        }
        setTitle(mClient.toString());
        updateActionBar();
    }

    /**
    *  if the user edit the client info, reload call log and texts
    * */
    private void updateCallnText() {
        for (int i = 1; i < 3; i++) {
            if (fragments[i] != null) {
                fragments[i].updateUI(mClient);
            }

        }
    }

    /**
     * if user save the change
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK) {
            updateUI();
            updateCallnText();
        }
    }


    @Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
        if (ClientContent.get(getApplicationContext()).delete(mClientId)) {
            Toast.makeText(this, "Client deleted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_edit:
                Intent intent = ClientEditActivity.newIntent(getApplicationContext(), mClientId);
                startActivityForResult(intent, REQUEST_EDIT);
                return true;
            case R.id.action_star:
                mClient.setStared(!mClient.isStared());
                updateActionBar();
                return true;

            case R.id.action_delete:
                FragmentManager manager = getSupportFragmentManager();
                DeleteAlertDialogFragment dialog = DeleteAlertDialogFragment.newInstance("client");
                dialog.show(manager, DIALOG_DELETE);
                return true;

            default:return super.onOptionsItemSelected(item);
        }
    }

    private void updateActionBar() {
        if (mStar != null) {
            mStar.setIcon(mClient.isStared() ? R.drawable.ic_star_yellow_500_24dp :
                    R.drawable.ic_star_outline_white_24dp);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        ClientContent.get(getApplicationContext()).updateClient(mClient);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client, menu);
        mStar = menu.findItem(R.id.action_star);
        updateActionBar();
        return true;
    }





}
