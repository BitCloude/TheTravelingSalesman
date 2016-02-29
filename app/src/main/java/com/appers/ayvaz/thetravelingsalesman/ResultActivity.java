package com.appers.ayvaz.thetravelingsalesman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.utils.ReportExportUtils;

import java.io.File;

public class ResultActivity extends AppCompatActivity {

    private static final String EXTRA_PATH = "extra_path";
    public static Intent newIntent(Context context, String path) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(EXTRA_PATH, path);
        return intent;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        String path = getIntent().getStringExtra(EXTRA_PATH);
//        ReportExportUtils.shareFile(this, new File(path));

//        if (!TextUtils.isEmpty(path)) {
//            Intent intent = ReportExportUtils.getOpenIntent(new File(path));
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            } else {
//                Toast.makeText(this, R.string.message_file_not_exit, Toast.LENGTH_SHORT).show();
//            }
//
//        }
    }
}
