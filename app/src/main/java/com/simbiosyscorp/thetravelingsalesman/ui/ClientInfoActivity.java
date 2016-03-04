package com.simbiosyscorp.thetravelingsalesman.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.models.Client;
import com.simbiosyscorp.thetravelingsalesman.models.ClientManager;
import com.simbiosyscorp.thetravelingsalesman.utils.CommUtils;
import com.simbiosyscorp.thetravelingsalesman.utils.PictureUtils;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ClientInfoActivity extends BaseActivity {

    private final String DEBUG_TAG = "ClientInfoActivity: ";
    private static final String EXTRA_CLIENT_ID = "client_id";
    private UUID mClientId;
    private Client mClient;
    private MenuItem mStar;
    private ViewGroup mCommContainer, mOtherContainer;
    private static final String DIALOG_DELETE = "DialogDelete";
    private final int REQUEST_DELETE = 2;
    private final int REQUEST_EDIT = 0;
    private LayoutInflater mInflater;
    @Bind (R.id.infoContainer)    LinearLayout mContainer;
    @Bind (R.id.clientPhoto)    ImageView mImageView;
    @Bind (R.id.toolbar_layout) CollapsingToolbarLayout mToolbarLayout;

    public static Intent newIntent(Context packageContext, UUID clientId) {
        Intent i = new Intent(packageContext, ClientInfoActivity.class);
        i.putExtra(EXTRA_CLIENT_ID, clientId);
        return i;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
                Intent intent = ClientActivity.newIntent(getApplicationContext(), mClientId);
                startActivity(intent);
            }
        });*/

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // setup client
        Serializable s = getIntent().getSerializableExtra(EXTRA_CLIENT_ID);
        mClientId = s == null ? null : (UUID) s;

        updateUI();

    }

    private void showPhoto() {
        File file = ClientManager.get(this).getPhotoFile(mClient, false);
        if (file == null || !file.exists()) {
            return;
        }
        Bitmap bitmap = PictureUtils.getScaledBitmap(
                file.getPath(), this);
        mImageView.setImageBitmap(bitmap);
    }

    private void updateUI() {
        mClient = ClientManager.get(this).getClient(mClientId);
        Log.i(DEBUG_TAG, "Client name: " + mClient.toString());

        if (mClient == null) {
            finish();
        }

        showPhoto();

        updateActionBar();


        mToolbarLayout.setTitle(mClient.toString());


        mInflater = getLayoutInflater();

        if (mContainer.getChildCount() != 0) {
            mContainer.removeAllViews();
            mOtherContainer = mCommContainer = null;

        }

        if (!TextUtils.isEmpty(mClient.getFirstPhone())) {
            addToView(R.layout.view_client_phone, 0);
            bindPhone(mCommContainer, mClient.getFirstPhone());
        }

        if (!TextUtils.isEmpty(mClient.getSecondPhone())) {
            addToView(R.layout.view_client_phone, 0);
            bindPhone(mCommContainer, mClient.getSecondPhone());
        }

        if (!TextUtils.isEmpty(mClient.getEmail())) {
            addToView(R.layout.view_client_email, 0);
            bindEmail(mCommContainer);
        }


        if (!TextUtils.isEmpty(mClient.getCompany())) {
            addToView(R.layout.view_client_other_info, 1);
            bindOtherInfo(mOtherContainer, getResources().getString(R.string.company), mClient.getCompany());
        }

        if (!TextUtils.isEmpty(mClient.getLinkedIn())) {
            addToView(R.layout.view_client_linkedin_row, 1);
            bindLinkedIn(mOtherContainer);
        }

        if (!TextUtils.isEmpty(mClient.getNote())) {
            addToView(R.layout.view_client_other_info, 1);
            bindOtherInfo(mOtherContainer, getResources().getString(R.string.note), mClient.getNote());
        }


        if (!TextUtils.isEmpty(mClient.getAddress())) {
            addToView(R.layout.view_client_other_info, 1);
            bindOtherInfo(mOtherContainer, getResources().getString(R.string.address), mClient.getAddress());
        }

        if (!TextUtils.isEmpty(mClient.getDesignation())) {
            addToView(R.layout.view_client_other_info, 1);
            bindOtherInfo(mOtherContainer, getResources().getString(R.string.designation),
                    mClient.getDesignation());
        }

        if (!TextUtils.isEmpty(mClient.getGroup())) {
            addToView(R.layout.view_client_other_info, 1);
            bindOtherInfo(mOtherContainer, getResources().getString(R.string.group),
                    mClient.getGroup());
        }

    }

    private void bindLinkedIn(ViewGroup parent) {
        View v = parent.getChildAt(parent.getChildCount() - 1);
        TextView field = (TextView) v.findViewById(R.id.content);
        field.setText(mClient.getLinkedInUrl());


        ImageButton button = (ImageButton) v.findViewById(R.id.linkedInButt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://" + mClient.getLinkedIn()));
                final PackageManager packageManager = getPackageManager();
                final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.isEmpty()) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mClient.getLinkedInUrl()));
                }
                startActivity(intent);

            }
        });
    }

    private void bindOtherInfo(ViewGroup parent, String name, String content) {
        View v = parent.getChildAt(parent.getChildCount()-1);
        TextView fieldName = (TextView) v.findViewById(R.id.fieldName);
        TextView field = (TextView) v.findViewById(R.id.content);

        fieldName.setText(name);
        field.setText(content);
    }

    private void bindEmail(ViewGroup parent) {
        View v = parent.getChildAt(parent.getChildCount()-1);
        TextView email = (TextView) v.findViewById(R.id.clientEmail);
        final String emailAddress = mClient.getEmail();
        email.setText(emailAddress);


        ImageButton sendEmail = (ImageButton) v.findViewById(R.id.emailButton);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommUtils.sendEmail(ClientInfoActivity.this, emailAddress);

            }
        });
    }

    private void bindPhone(ViewGroup parent, String number) {
        View view = parent.getChildAt(parent.getChildCount()-1);
        final TextView num = (TextView) view.findViewById(R.id.client_email);
        num.setText(number);
        ImageButton call = (ImageButton) view.findViewById(R.id.callButton);
        ImageButton text = (ImageButton) view.findViewById(R.id.textButton);
        final String number1 = number;

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommUtils.dial(ClientInfoActivity.this, number1);
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommUtils.sendText(ClientInfoActivity.this, number1);     }
        });
    }

    private void addToView(int resId, int group) {
        ViewGroup container = group == 0 ? mCommContainer : mOtherContainer;

        if (container == null) {
            if (group == 1 && mCommContainer != null) {
                mInflater.inflate(R.layout.view_divider_horizontal, mContainer);
            }


            container = (ViewGroup) mInflater.inflate(R.layout.view_client_holder, mContainer);
            if (group == 0) {
                mCommContainer = container;
            } else {
                mOtherContainer = container;
            }

        }


        mInflater.inflate(resId, container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                Intent upIntent = ClientActivity.newIntent(this, mClientId);
                startActivity(upIntent);
                finish();
                Log.i("...........", "Back");
                return true;

            case R.id.action_edit:
                Intent intent = ClientEditActivity.newIntent(this, mClientId);
                startActivityForResult(intent, REQUEST_EDIT);
                return true;
            case R.id.action_star:
                mClient.setStared(!mClient.isStared());
                updateActionBar();
                return true;

            case R.id.action_delete:
                alertDelete();
//                FragmentManager manager = getSupportFragmentManager();
//                DeleteAlertDialogFragment dialog = DeleteAlertDialogFragment.newInstance("client");
//
//                dialog.show(manager, DIALOG_DELETE);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    private void alertDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClientInfoActivity.this);
        builder.setMessage("Do you want to delete this client?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ClientManager.get(ClientInfoActivity.this).delete(mClientId)) {
                            Toast.makeText(ClientInfoActivity.this, "Client deleted", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                })
        .setNegativeButton(android.R.string.cancel, null);
        AlertDialog dialog = builder.create();

        dialog.show();

    }

    private void updateActionBar() {
        if (mStar != null) {
            mStar.setIcon(mClient.isStared() ? R.drawable.ic_star_yellow_500_24dp :
                    R.drawable.ic_star_outline_white_24dp);
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client_info, menu);
        mStar = menu.findItem(R.id.action_star);
        updateActionBar();
        return true;
    }

    @Override
    protected void onPause() {
        ClientManager.get(this).updateClient(mClient);
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DELETE) {
            if (ClientManager.get(this).delete(mClientId)) {
                Toast.makeText(this, "Client deleted", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /*@Override
    public void onDialogPositiveClick(android.support.v4.app.DialogFragment dialog) {
        if (ClientManager.get(getApplicationContext()).delete(mClientId)) {
            Toast.makeText(this, "Client deleted", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {

    }*/

}
