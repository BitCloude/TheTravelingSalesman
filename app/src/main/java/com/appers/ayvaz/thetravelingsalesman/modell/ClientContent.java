package com.appers.ayvaz.thetravelingsalesman.modell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.ClientListFragment;
import com.appers.ayvaz.thetravelingsalesman.database.ClientCursorWrapper;
import com.appers.ayvaz.thetravelingsalesman.database.DatabaseHelper;
import com.appers.ayvaz.thetravelingsalesman.database.DbSchema.ClientTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* A singleton class to provide access to Clients
*
* */
public class ClientContent {
/*
    private List<Client> ITEMS;
    private Random random;
    private Map<Integer, Client> ITEM_MAP;
    private static final int COUNT = 25;
    */

    private static ClientContent content;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ClientContent(Context context) {
        mContext = context;
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

    public static ClientContent get(Context context) {
        if (content == null) {
            content = new ClientContent(context);
        }

        return content;
    }

    private static ContentValues getContentValues(Client client) {
        ContentValues values = new ContentValues();
        values.put(ClientTable.Cols.UUID, client.getId().toString());
        values.put(ClientTable.Cols.LAST_NAME, client.getLastName());
        values.put(ClientTable.Cols.FIRST_NAME, client.getFirstName());
        values.put(ClientTable.Cols.EMAIL, client.getEmail());
        values.put(ClientTable.Cols.ADDRESS, client.getAddress());
        values.put(ClientTable.Cols.COMPANY, client.getCompany());
        values.put(ClientTable.Cols.NOTE, client.getNote());
        values.put(ClientTable.Cols.FIRST_PHONE, client.getFirstPhone());
        values.put(ClientTable.Cols.SECOND_PHONE, client.getSecondPhone());
        values.put(ClientTable.Cols.STARED, client.isStared() ? 1 : 0);
        values.put(ClientTable.Cols.LINKEDIN, client.getLinkedIn());
        values.put(ClientTable.Cols.IMAGE, client.getImage());

        return values;
    }

    public Client getClient(UUID id) {
        ClientCursorWrapper cursor = queryClients(
                ClientTable.Cols.UUID + " = ?",
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
        return getClients(ClientListFragment.ALL);
    }

    public List<Client> getClients(int range) {
        List<Client> clients = new ArrayList<>();
        String whereClause = null;
        String[] whereArgs = null;
        String sortOrder = ClientTable.Cols.LAST_NAME;

        if (range == ClientListFragment.FAVORITE) {
            whereClause = ClientTable.Cols.STARED + " = 1";
        } else if (range == ClientListFragment.RECENT) {
            Toast.makeText(mContext, "Not implemented", Toast.LENGTH_SHORT).show();
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
        ContentValues values = getContentValues(item);
        mDatabase.insert(ClientTable.NAME, null, values);
    }

    public void updateClient(Client c) {
        String uuidString = c.getId().toString();
        ContentValues values = getContentValues(c);

        mDatabase.update(ClientTable.NAME, values,
                ClientTable.Cols.UUID + " =  ?",
                new String[]{uuidString});
    }

    private ClientCursorWrapper queryClients(String whereClause, String[] whereArgs,
                                             String sortOrder) {
        Cursor cursor = mDatabase.query(
                ClientTable.NAME,
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
        String whereClause = ClientTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{uuid.toString()};
        return mDatabase.delete(ClientTable.NAME, whereClause, whereArgs) > 0;
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
