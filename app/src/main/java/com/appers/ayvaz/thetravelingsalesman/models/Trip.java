package com.appers.ayvaz.thetravelingsalesman.models;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Trip {
    private int id , client_id;

    private String type, trip_from, trip_to,date_from,
            date_to, description;
    private byte[] image;


    public Trip(int id) {
        this.id = id;
    }

    public int getId(){ return id;}

    public void setClient_id(int client_id){this.client_id = client_id;}

    public int getClient_id(){return client_id;}

    public void setType(String type){this.type = type;}

    public String getType(){return type;}

    public void setTrip_from(String trip_from){this.trip_from = trip_from;}

    public String getTrip_from(){return trip_from;}

    public void setTrip_to(String trip_to){this.trip_to = trip_to;}

    public String getTrip_to(){return trip_to;}

    public void setDate_from(String date_from){this.date_from = date_from;}

    public String getDate_from(){return date_from;}

    public void setDate_to(String date_to){this.date_to = date_to;}

    public String getDate_to(){return date_to;}

    public void setDescription(String description){this.description = description;}

    public String getDescription(){return description;}

    public void setImage(byte[] image) {this.image = image;}

    public byte[] getImage() {return image;}


}
