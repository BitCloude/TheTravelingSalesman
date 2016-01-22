package com.appers.ayvaz.thetravelingsalesman.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by D on 017 01/17.
 */
public class CommUtils {
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void sendEmail(Context context, String address) {
        if (!isValidEmail(address)) {
            Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void dial(Context context, String number) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: " + number));
        context.startActivity(dialIntent);
    }

    public static void sendText(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", number);

        intent.setType("vnd.android-dir/mms-sms");


        context.startActivity(intent);
    }
}
