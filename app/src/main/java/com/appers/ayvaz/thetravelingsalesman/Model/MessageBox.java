package com.appers.ayvaz.thetravelingsalesman.Model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;

import com.appers.ayvaz.thetravelingsalesman.adapter.MessageAdapterSlow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by D on 030 12/30.
 */
public class MessageBox {
    private static MessageBox mMessageBox;
    final String[] reqCols = {"_id", "address", "body", "date"};
    private final Uri inboxURI = Uri.parse("content://sms/inbox");
    private final Uri sentURI = Uri.parse("content://sms/sent");
    String sortOrder = SmsMms.Cols.DATE + " desc";
    private Context mContext;
    private String mNumber1, mNumber2;
    private boolean msgChanged = false;
    private MessageAdapterSlow mAdapter;
    private List<SmsMms> messageList;

    private MessageBox(Context context) {
        mContext = context;

    }

    ;

    public static MessageBox get(Context context) {
        if (mMessageBox == null) {
            mMessageBox = new MessageBox(context);
        }
        return mMessageBox;
    }

    public MessageAdapterSlow query(String number1, String number2) {
        mNumber1 = number1;
        mNumber2 = number2;
        messageList = new ArrayList<SmsMms>();
        Cursor cursor = mContext.getContentResolver().query(inboxURI,
                reqCols, null, null, sortOrder);
        read(cursor, SmsMms.INBOX);

        cursor = mContext.getContentResolver().query(sentURI,
                reqCols, null, null, sortOrder);

        read(cursor, SmsMms.SENT);

        Collections.sort(messageList);
        return new MessageAdapterSlow(mContext, messageList);

    }

    private boolean read(Cursor cursor, int type) {
        if (cursor == null) {
            return false;
        }

        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndex(
                    SmsMms.Cols.ADDRESS
            ));
            if (PhoneNumberUtils.compare(mNumber1, number) ||
                    PhoneNumberUtils.compare(mNumber2, number)) {
                SmsMms message = SmsMms.fromCursor(cursor, type);
                messageList.add(message);
            }

        }
        cursor.close();
        return true;

    }
}
