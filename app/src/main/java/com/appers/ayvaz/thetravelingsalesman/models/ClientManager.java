package com.appers.ayvaz.thetravelingsalesman.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.ClientListFragment;
import com.appers.ayvaz.thetravelingsalesman.database.ClientCursorWrapper;
import com.appers.ayvaz.thetravelingsalesman.database.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.appers.ayvaz.thetravelingsalesman.database.DbSchema.ClientTable.*;

/*
* A singleton class to provide access to Clients
*
* */
public class ClientManager {
/*
    private List<Client> ITEMS;
    private Random random;
    private Map<Integer, Client> ITEM_MAP;
    private static final int COUNT = 25;
    */

    private static ClientManager content;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    public static final String AND = " AND";
    public static final String OR = " OR ";
    public static final String LIKE = " LIKE ?";

    private ClientManager(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DatabaseHelper(mContext)
                .getWritableDatabase();

        /*
        ITEMS = new ArrayList<Client>();
        ITEM_MAP = new HashMap<Integer, Client>();
        random = new Random();
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addClient(createDummyItem(i));
        }
        */
    }

    public static ClientManager get(Context context) {
        if (content == null) {
            content = new ClientManager(context);
        }

        return content;
    }

    private static ContentValues getContentValues(Client client) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, client.getId().toString());
        values.put(Cols.LAST_NAME, client.getLastName());
        values.put(Cols.FIRST_NAME, client.getFirstName());
        values.put(Cols.EMAIL, client.getEmail());
        values.put(Cols.ADDRESS, client.getAddress());
        values.put(Cols.COMPANY, client.getCompany());
        values.put(Cols.NOTE, client.getNote());
        values.put(Cols.FIRST_PHONE, client.getFirstPhone());
        values.put(Cols.SECOND_PHONE, client.getSecondPhone());
        values.put(Cols.STARED, client.isStared() ? 1 : 0);
        values.put(Cols.LINKEDIN, client.getLinkedIn());
//        values.put(ClientTable.Cols.IMAGE, client.getImage());

        return values;
    }

    public Client getClient(UUID id) {
        ClientCursorWrapper cursor = queryClients(
                Cols.UUID + " = ?",
                new String[]{id.toString()}, null
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getClient();
        } finally {
            cursor.close();
        }
    }

    public List<Client> getClients() {
        return getClients(ClientListFragment.RANGE_ALL);
    }

    public List<Client> getClients(int range) {
        List<Client> clients = new ArrayList<>();
        String whereClause = null;
        String[] whereArgs = null;
        String sortOrder = Cols.LAST_NAME;

        if (range == ClientListFragment.RANGE_FAVORITE) {
            whereClause = Cols.STARED + " = 1";
        } else if (range == ClientListFragment.RANGE_RECENT) {
            Log.i("client", "recent not implemented");
        }

        try (ClientCursorWrapper cursor = queryClients(whereClause, whereArgs,
                sortOrder)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                clients.add(cursor.getClient());
                cursor.moveToNext();
            }
        }

        return clients;
    }

    public void addClient(Client item) {
        ContentValues values = item.getContentValues();//getContentValues(item);
        mDatabase.insert(NAME, null, values);
    }

    public void updateClient(Client c) {
        String uuidString = c.getId().toString();
        ContentValues values = c.getContentValues();//getContentValues(c);

        mDatabase.update(NAME, values,
                Cols.UUID + " =  ?",
                new String[]{uuidString});
    }

    private ClientCursorWrapper queryClients(String whereClause, String[] whereArgs,
                                             String sortOrder) {
        Cursor cursor = mDatabase.query(
                NAME,
                null, // Columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, // having
                sortOrder // orderBy
        );

        return new ClientCursorWrapper(cursor);
    }


    public boolean delete(UUID uuid) {
        String whereClause = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{uuid.toString()};
        return mDatabase.delete(NAME, whereClause, whereArgs) > 0;
    }

    public File getPhotoFile(Client client, boolean tmp) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        Log.i("......", "Path: " + externalFilesDir.getPath());
        return new File(externalFilesDir, client.getPhotoFileName(tmp));
    }

    public List<Client> getSearchResult(String query) {
        String[] columns = new String[]{
                Cols.FIRST_NAME,
                Cols.LAST_NAME,
                Cols.FIRST_PHONE,
                Cols.SECOND_PHONE,
                Cols.EMAIL,
                Cols.UUID
        };

        String selection = Cols.FIRST_NAME + LIKE
                + OR + Cols.LAST_NAME + LIKE
                + OR + Cols.FIRST_PHONE + LIKE
                + OR + Cols.SECOND_PHONE + LIKE
                + OR + Cols.EMAIL + LIKE;

        String filter = "%" + query + "%";
        Log.i("........", query);
        String[] selectionArgs = new String[] {filter, filter, filter, filter, filter};
//        String[] selectionArgs = new String[]{filter};

        Cursor cursor = query(selection, selectionArgs, null);
        List<Client> result = new ArrayList<Client>();

        if (cursor == null || cursor.getCount() == 0) {
            return result;
        }

        ClientCursorWrapper cursorWrapper = new ClientCursorWrapper(cursor);

        while (cursorWrapper.moveToNext()) {
            result.add(cursorWrapper.getClient());
        }

        return result;

    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {


        Cursor cursor = mDatabase.query(NAME,
                columns, selection, selectionArgs, null, null, null);



        return cursor;
    }

/*
    private String makeDetails(int position) {
        return random.nextInt() + "@gmail.com";
    }
    private Client createDummyItem(int position) {
        Client client = new Client();
        client.setFirstName("Anthony");
        client.setLastName("Cashmore");
        client.setEmail(makeDetails(position));
        client.setFirstPhone("415-567-1234");
        client.setCompany("Company Name");
        return client;
    }
    */
}
