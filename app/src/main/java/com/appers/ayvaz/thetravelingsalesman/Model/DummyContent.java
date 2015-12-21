package com.appers.ayvaz.thetravelingsalesman.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    private static List<Client> ITEMS ;
    private static Random random;
    private static DummyContent content;
    private static Map<Integer, Client> ITEM_MAP;


    public static DummyContent get() {
        if (content == null) {
            content = new DummyContent();
        }

        return content;
    }
    private DummyContent() {
        ITEMS = new ArrayList<Client>();
        ITEM_MAP = new HashMap<Integer, Client>();
        random = new Random();
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }



    public static Client getItem(int id) {
        return ITEM_MAP.get(id);
    }
    private static final int COUNT = 25;

    public static List<Client> getItems() {
        return get().ITEMS;
    }
    private static void addItem(Client item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    private static Client createDummyItem(int position) {
        Client client = new Client(position);
        client.setFirstName("Anthony");
        client.setLastName("Cashmore");
        client.setEmail(makeDetails(position));
        client.setMobile("415-523-1234");

        return client;
    }

    private static String makeDetails(int position) {
        return random.nextInt()+"@gmail.com";
    }

/*
    public static Task createDummyTask(int position) {
        int day = 1 + position%30;
        return new Task("10/"+day, "11/"+day);
    }*/
}
