package com.appers.ayvaz.thetravelingsalesman;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import java.io.Serializable;
import java.util.UUID;

public class ClientEditActivity extends SingleFragmentActivityNoNav {


    private static final String EXTRA_CLIENT_ID = "com.appers.ayvaz.thetravelingsalesman.client_id";


    public static Intent newIntent(Context packageContext, UUID clientId) {
        Intent intent = new Intent(packageContext, ClientEditActivity.class);
        intent.putExtra(EXTRA_CLIENT_ID, clientId);
        return intent;
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ClientEditActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        Serializable s = getIntent().getSerializableExtra(EXTRA_CLIENT_ID);
        UUID clientId = s == null ? null : (UUID) s;
        return ClientEditFragment.newInstance(clientId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.setContentView(R.layout.activity_client_add);

        setTitle(R.string.title_activity_client_add);
    }


//
//    public void showDatePicker(View view) {
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int date = c.get(Calendar.DATE);
//        final EditText dateText = (EditText) findViewById(R.id.client_dob);
//        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                dateText.setText(monthOfYear + "/" + dayOfMonth+  "/" + year  );
//            }
//        }, year, month, date);
//        dialog.show();
//    }
}
