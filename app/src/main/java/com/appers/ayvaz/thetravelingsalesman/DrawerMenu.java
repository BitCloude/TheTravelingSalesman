package com.appers.ayvaz.thetravelingsalesman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by D on 005 01/05.
 */
public class DrawerMenu {



    public static void selectItem(Activity activity, int itemId) {

        if (itemId == R.id.nav_reports) {
            activity.startActivity(new Intent(activity, ReportsActivity.class));
        } else if (itemId == R.id.nav_trip) {
            activity.startActivity(new Intent(activity, TripExpMan.class));
        } else if (itemId == R.id.nav_tasks) {
            activity.startActivity(new Intent(activity, TaskListActivity.class));
        } else if (itemId == R.id.nav_notifications) {
            activity.startActivity(new Intent(activity, NotificationActivity.class));
        } else if (itemId == R.id.nav_clients) {
            activity.startActivity(new Intent(activity, LandingActivity.class));
        }

    }

}
