package com.appers.ayvaz.thetravelingsalesman.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class Trip implements Parcelable {
    private int id;
    private UUID client_id;

    private String type, trip_from, trip_to,date_from,
            date_to, boarding, description;
    private byte[] image;

    public Trip(){}

    public Trip(int id) {this.id = id;
    }

    public String toString() {
        if (!isEmpty(trip_from) || !isEmpty(trip_to)) {
            return trip_from +
                    (isEmpty(trip_from) ? "" : " ")
                    + trip_to;
        }

        if (!isEmpty(description)) {
            return description;
        }

            return boarding;
    }

    private boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    public int getId(){ return id;}

    public void setClient_id(UUID client_id){this.client_id = client_id;}

    public UUID getClient_id(){return client_id;}

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

    public void setBoarding(String boarding){this.boarding = boarding;}

    public String getBoarding(){return boarding;}

    public void setDescription(String description){this.description = description;}

    public String getDescription(){return description;}

    public void setImage(byte[] image) {this.image = image;}

    public byte[] getImage() {return image;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeSerializable(client_id);
        dest.writeString(type);
        dest.writeString(trip_from);
        dest.writeString(trip_to);
        dest.writeString(date_from);
        dest.writeString(date_to);
        dest.writeString(boarding);
        dest.writeString(description);
        dest.writeByteArray(image);

    }

    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    private Trip(Parcel in) {
        this.id = in.readInt();
        this.client_id = (UUID) in.readSerializable();
        this.type = in.readString();
        this.trip_from = in.readString();
        this.trip_to = in.readString();
       this.date_from = in.readString();
        this.date_to = in.readString();
        this.boarding = in.readString();
        this.description = in.readString();
        this.image = in.createByteArray();

    }

}
