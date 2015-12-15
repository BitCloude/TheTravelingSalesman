package com.appers.ayvaz.thetravelingsalesman.dummy;

import com.appers.ayvaz.thetravelingsalesman.Model.Client;
import com.appers.ayvaz.thetravelingsalesman.Model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Client> ITEMS = new ArrayList<Client>();
    public static final List<Task> TASKS = new ArrayList<>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Integer, Client> ITEM_MAP = new HashMap<Integer, Client>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
            TASKS.add(createDummyTask(i));
        }
    }

    private static void addItem(Client item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Client createDummyItem(int position) {
        return new Client(position,"Dummy", "Number"+position, makeDetails(position), "Company Name");

    }

    private static String makeDetails(int position) {
        return position+"@gmail.com";
    }


    public static Task createDummyTask(int position) {
        int day = 1 + position%30;
        return new Task("10/"+day, "11/"+day);
    }
}
