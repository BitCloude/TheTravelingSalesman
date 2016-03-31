package com.simbiosyscorp.thetravelingsalesman.models;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Data;
import android.text.TextUtils;
import android.util.Log;

import com.simbiosyscorp.thetravelingsalesman.database.ClientCursorWrapper;
import com.simbiosyscorp.thetravelingsalesman.database.DatabaseHelper;
import com.simbiosyscorp.thetravelingsalesman.database.DbSchema;
import com.simbiosyscorp.thetravelingsalesman.view.ClientListFragment;
import com.simbiosyscorp.thetravelingsalesman.utils.PictureUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.simbiosyscorp.thetravelingsalesman.database.DbSchema.ClientTable.Cols;
import static com.simbiosyscorp.thetravelingsalesman.database.DbSchema.ClientTable.NAME;

/*
* A singleton class to provide access to Clients
*
* */
public class ClientManager {
    public static final String AND = " AND";
    /*
        private List<Client> ITEMS;
        private Random random;
        private Map<Integer, Client> ITEM_MAP;
        private static final int COUNT = 25;
        */
    public static final String OR = " OR ";
    public static final String LIKE = " LIKE ?";
    private static final String DEBUGTAG = "ClientManager";

    private static final String[] PHONE_PROJ =
            new String[]{Data._ID, CommonDataKinds.Phone.NUMBER,
                    CommonDataKinds.Phone.TYPE, CommonDataKinds.Phone.LABEL};

    private static final String[] EMAIL_PROJ =
            new String[]{
                    CommonDataKinds.Email.TYPE,
                    CommonDataKinds.Email.ADDRESS,
                    CommonDataKinds.Email.LABEL
            };

    private static final String[] ORG_PROJ =
            new String[]{
                    CommonDataKinds.Organization.COMPANY,
                    CommonDataKinds.Organization.TITLE
            };
    private static final String RECENT_LIMIT = "10";


    private static ClientManager content;
    private final String[] CONTACT_PROJ = new String[]{
            ContactsContract.Contacts._ID
    };
    private Context mContext;
    private SQLiteDatabase mDatabase;

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
        values.put(Cols.CONTACT_ID, client.getContactId());
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
            return getRecentClient();
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
        String[] selectionArgs = new String[]{filter, filter, filter, filter, filter};
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
// Retrieves the profile from the Contacts Provider

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {


        Cursor cursor = mDatabase.query(NAME,
                columns, selection, selectionArgs, null, null, null);


        return cursor;
    }

    public String getContactId(Uri contactUri) {
        ContentResolver cr = mContext.getContentResolver();
        Cursor contactCursor = cr.query(
                contactUri,
                CONTACT_PROJ,
                null,
                null,
                null);

        if (contactCursor != null && contactCursor.moveToFirst()) {

            Log.i(DEBUGTAG, "Column count: " + contactCursor.getColumnCount());

            String id = contactCursor.getString(0);
            Log.i(DEBUGTAG, contactCursor.getColumnName(0) + ": " + id);
            contactCursor.close();
            return id;
        }

        return null;
    }

    public String getDisplayNameFromContact(Uri contactUri) {
        String id = getContactId(contactUri);
        return getDisplayNameFromContact(id);
    }

    public String getDisplayNameFromContact(String id) {

        if (id != null) {
            ContentResolver cr = mContext.getContentResolver();
            Cursor cursor = cr.query(Data.CONTENT_URI,
                    new String[]{
                            CommonDataKinds.StructuredName.DISPLAY_NAME
                    },
                    Data.CONTACT_ID + "=?" + " AND "
                            + Data.MIMETYPE + "='" + CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'",
                    new String[]{id}, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String displayName = cursor.getString(0);
                    cursor.close();
                    return displayName;
                }
                cursor.close();
            }
        }

        return null;
    }

    public UUID getClientId(String contactId) {
        Cursor cursor = queryClientByContactId(contactId);
        if (cursor != null) {
            UUID uuid = null;
            if (cursor.moveToFirst()) {
                uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(Cols.UUID)));
                Log.i(DEBUGTAG, "uuid: " + uuid);
            }
            cursor.close();
            return uuid;
        }

        return null;
    }

    public Client getClientFromContact(String id, UUID uuid) {
        if (id == null) {
            return null;
        }


        ContentResolver cr = mContext.getContentResolver();
        // use contact id to get phone, email and other information
        final String[] selectionArg = new String[]{id};

        Log.i(DEBUGTAG, "uuid param: " + uuid);

        Client client = uuid == null ? new Client() : getClient(uuid);

        Log.i(DEBUGTAG, "new client id: " + client.getId());

        // contact id
        client.setContactId(id);

        // name
        Cursor cursor = cr.query(Data.CONTENT_URI,
                new String[]{
                        CommonDataKinds.StructuredName.FAMILY_NAME,
                        CommonDataKinds.StructuredName.GIVEN_NAME
                },
                Data.CONTACT_ID + "=?" + " AND "
                        + Data.MIMETYPE + "='" + CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'",
                selectionArg, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                if (cursor.getString(0) != null) {

                    client.setLastName(cursor.getString(0));
                } else {
                    client.setLastName("");
                }


                if (cursor.getString(1) != null) {

                    client.setFirstName(cursor.getString(1));
                } else {
                    client.setFirstName("");
                }


            }
            cursor.close();
        }

        Log.i(DEBUGTAG, "first name: " + client.getFirstName());
        Log.i(DEBUGTAG, "last name: " + client.getLastName());


        // phone: only import the first two, ignore label
        cursor = cr.query(Data.CONTENT_URI,
                PHONE_PROJ,
                Data.CONTACT_ID + "=?" + " AND "
                        + Data.MIMETYPE + "='" + CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'",
                selectionArg, null);

        if (cursor != null) {
            Log.i(DEBUGTAG, "phone count: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                client.setFirstPhone(cursor.getString(1));
                if (cursor.moveToNext()) {
                    client.setSecondPhone(cursor.getString(1));
                } else {
                    client.setSecondPhone("");
                }
            } else {
                client.setFirstPhone("");
                client.setSecondPhone("");
            }

            cursor.close();
        }
        Log.i(DEBUGTAG, "phone: " + client.getFirstPhone() + ", " + client.getSecondPhone());

        // email: only import the first one
        cursor = cr.query(Data.CONTENT_URI,
                EMAIL_PROJ,
                Data.CONTACT_ID + "=?" + " AND "
                        + Data.MIMETYPE + "='" + CommonDataKinds.Email.CONTENT_ITEM_TYPE + "'",
                selectionArg,
                null);

        if (cursor != null) {
            Log.i(DEBUGTAG, "email count: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                client.setEmail(cursor.getString(1));
            } else {
                client.setEmail("");
            }

            cursor.close();
        }

        // postal address
        cursor = cr.query(Data.CONTENT_URI,
                new String[]{
                        CommonDataKinds.StructuredPostal.STREET,
                        CommonDataKinds.StructuredPostal.POBOX,
                        CommonDataKinds.StructuredPostal.NEIGHBORHOOD,
                        CommonDataKinds.StructuredPostal.CITY,
                        CommonDataKinds.StructuredPostal.REGION,
                        CommonDataKinds.StructuredPostal.POSTCODE,
                        CommonDataKinds.StructuredPostal.COUNTRY
                },
                Data.CONTACT_ID + "=?" + " AND "
                        + Data.MIMETYPE + "='" + CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE + "'",
                selectionArg,
                null);

        if (cursor != null) {
            Log.i(DEBUGTAG, "addr cnt: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String curr = cursor.getString(i);
                    if (!TextUtils.isEmpty(curr)) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(curr);

                    }
                }

                Log.i(DEBUGTAG, sb.toString());
                client.setAddress(sb.toString());
            } else {
                client.setAddress("");
            }
        }

        // company and designation (Company and title)
        cursor = cr.query(Data.CONTENT_URI,
                ORG_PROJ,
                Data.CONTACT_ID + "=?" + " AND "
                        + Data.MIMETYPE + "='" + CommonDataKinds.Organization.CONTENT_ITEM_TYPE + "'",
                selectionArg,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                if (cursor.getString(0) != null)
                    client.setCompany(cursor.getString(0));
                else {
                    client.setCompany("");
                }
                if (cursor.getString(1) != null)
                    client.setDesignation(cursor.getString(1));
                else {
                    client.setDesignation("");
                }
                Log.i(DEBUGTAG, "company: " + client.getCompany() + "title: "
                        + client.getDesignation());
            }
        }

        // website/LinkedIn
        cursor = cr.query(Data.CONTENT_URI,
                new String[]{CommonDataKinds.Website.URL},
                Data.CONTACT_ID + "=?" + " AND "
                        + Data.MIMETYPE + "='" + CommonDataKinds.Website.CONTENT_ITEM_TYPE + "'",
                selectionArg,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String url = cursor.getString(0);
                Log.i(DEBUGTAG, "website: " + url);
                if (url.startsWith("http://www.linkedin.com")
                        || url.startsWith("https://www.linkedin.com")
                        || url.startsWith("linkedin.com")) {
                    Log.i(DEBUGTAG, "is linkedIn");
                    client.setLinkedIn(url);
                } else {
                    client.setLinkedIn("");
                }
            } else {
                client.setLinkedIn("");
            }
            cursor.close();
        }

        // note
        cursor = cr.query(Data.CONTENT_URI,
                new String[]{CommonDataKinds.Note.NOTE},
                Data.CONTACT_ID + "=?" + " AND "
                        + Data.MIMETYPE + "='" + CommonDataKinds.Note.CONTENT_ITEM_TYPE + "'",
                selectionArg,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Log.i(DEBUGTAG, "Note: " + cursor.getString(0));
                client.setNote(cursor.getString(0));
            } else {
                client.setNote("");
            }
            cursor.close();
        }

        //get photo
        cursor = cr.query(Data.CONTENT_URI,
                new String[]{CommonDataKinds.Phone.PHOTO_URI},
                Data.CONTACT_ID + "=?" + " AND "
                        + Data.MIMETYPE + "='" + CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'",
                selectionArg,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String s = cursor.getString(0);
                Log.i(DEBUGTAG, "photo uri: " + s);
                if (!TextUtils.isEmpty(s)) {
                    Uri photoUri = Uri.parse(cursor.getString(0));
                    try {
                        AssetFileDescriptor fd = cr.openAssetFileDescriptor(
                                photoUri, "r");
                        if (fd != null) {
                            InputStream in = fd.createInputStream();
                            File to = getPhotoFile(client, false);
                            PictureUtils.copyFile(in, to);
                        }

                    } catch (IOException e) {
                        return null;
                    }
                }



            }

            cursor.close();
        }


        return client;
    }

    private Cursor queryClientByContactId(String contactId) {
        return query(Cols.CONTACT_ID + "=?", new String[]{contactId}, null);
    }

    public List<Client> getRecentClient() {
        List<Client> clients = new ArrayList<>();

        String query = String.format("SELECT * FROM %s INNER JOIN %s ON %s = %s GROUP BY %s "
                + "ORDER BY %s DESC LIMIT %s",
                DbSchema.ClientTable.NAME,
                DbSchema.TaskTable.NAME,
                Cols.UUID,
                DbSchema.TaskTable.Cols.CLIENT_ID,
                Cols.UUID,
                DbSchema.TaskTable.Cols.EVENT_ID,
                RECENT_LIMIT);

        String[] args = new String[] {
                /*Cols._ID,
                Cols.COMPANY,
                Cols.EMAIL,
                Cols.FIRST_NAME,
                Cols.LAST_NAME,
                Cols.FIRST_PHONE,
                Cols.SECOND_PHONE,
                Cols.NOTE,
                Cols.LINKEDIN,
                DbSchema.TaskTable.Cols.EVENT_ID*/
        };

        Cursor cursor = mDatabase.rawQuery(query, args);

        if (cursor != null) {
            ClientCursorWrapper c = new ClientCursorWrapper(cursor);

            while (c.moveToNext()) {
                Client client = c.getClient();
                clients.add(client);
                Log.i(DEBUGTAG, "-----");
                Log.i(DEBUGTAG, "name: " + c.getString(c.getColumnIndex(Cols.FIRST_NAME)));
                Log.i(DEBUGTAG, "event id: " + c.getString(c.getColumnIndex(DbSchema.TaskTable.Cols.EVENT_ID)));
            }

            c.close();
        }

//        RecentClient r = new RecentClient(new Client());
//        clients.add(r);

        return clients;
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
