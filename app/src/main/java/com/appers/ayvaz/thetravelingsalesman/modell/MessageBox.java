package com.appers.ayvaz.thetravelingsalesman.modell;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
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

    final String[] callLogCols = {android.provider.CallLog.Calls.NUMBER,
            android.provider.CallLog.Calls.TYPE,
            android.provider.CallLog.Calls.DATE,
            android.provider.CallLog.Calls.DURATION};

    private final Uri inboxURI = Uri.parse("content://sms/inbox");
    private final Uri sentURI = Uri.parse("content://sms/sent");
    String sortOrder = MyMessage.Cols.DATE + " desc";
    private Context mContext;
    private String mNumber1, mNumber2;
    private boolean msgChanged = false;
    private MessageAdapterSlow mAdapter;
    private List<MyMessage> messageList;

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

    public List<CallEntry> queryCallLog(String num1, String num2) {
        List<CallEntry> callEntryList = new ArrayList<>();

//        String mSelectionClause = CallLog.Calls.NUMBER+ " >= ?";
        String mSelectionClause = null;
        String[] mSelectionArgs = null;

        Cursor mCallCursor = mContext.getContentResolver().query(
                android.provider.CallLog.Calls.CONTENT_URI,
                callLogCols,
                mSelectionClause,
                mSelectionArgs,
                android.provider.CallLog.Calls.DATE + " DESC"
                );

        Cursor cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, callLogCols, null, null, null);
        if (cursor == null) {
            return callEntryList;
        }

        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndex(
                    CallLog.Calls.NUMBER
            ));

            if (PhoneNumberUtils.compare(num1, number) ||
                    PhoneNumberUtils.compare(num2, number)) {
                CallEntry call = CallEntry.fromCursor(cursor);
                callEntryList.add(call);
            }

        }
        cursor.close();
        return callEntryList;
    }

    public MessageAdapterSlow query(String number1, String number2) {
        mNumber1 = number1;
        mNumber2 = number2;
        messageList = new ArrayList<MyMessage>();

        readMessage(MyMessage.INBOX);
        readMessage(MyMessage.SENT);

        Collections.sort(messageList);
        return new MessageAdapterSlow(mContext, messageList);

    }

    private boolean readMessage(int type) {
        Uri uri = type == MyMessage.SENT ? sentURI : inboxURI;

        Cursor cursor = mContext.getContentResolver().query(uri, reqCols, null, null, null);
        if (cursor == null) {
            return false;
        }

        while (cursor.moveToNext()) {
            String number = cursor.getString(cursor.getColumnIndex(
                    MyMessage.Cols.ADDRESS
            ));

            if (PhoneNumberUtils.compare(mNumber1, number) ||
                    PhoneNumberUtils.compare(mNumber2, number)) {
                MyMessage message = MyMessage.fromCursor(cursor, type);
                messageList.add(message);
            }

        }
        cursor.close();
        return true;
    }


}
