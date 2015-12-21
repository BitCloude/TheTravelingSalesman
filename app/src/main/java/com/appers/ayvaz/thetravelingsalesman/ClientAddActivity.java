package com.appers.ayvaz.thetravelingsalesman;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.appers.ayvaz.thetravelingsalesman.Views.SingleFragmentActivity;

import java.util.Calendar;

public class ClientAddActivity extends SingleFragmentActivity {


    private static final String EXTRA_CLIENT_ID = "com.appers.ayvaz.thetravelingsalesman.client_id";

    public static Intent newIntent(Context packageContext, int clientId) {
        Intent intent = new Intent(packageContext, ClientAddActivity.class);
        intent.putExtra(EXTRA_CLIENT_ID, clientId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int clientId = (int) getIntent().getSerializableExtra(EXTRA_CLIENT_ID);
        return ClientAddFragment.newInstance(clientId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_client_add);

        setTitle(R.string.title_activity_client_add);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    public void showDatePicker(View view) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        final EditText dateText = (EditText) findViewById(R.id.client_dob);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateText.setText(monthOfYear + "/" + dayOfMonth+  "/" + year  );
            }
        }, year, month, date);
        dialog.show();
    }
}
