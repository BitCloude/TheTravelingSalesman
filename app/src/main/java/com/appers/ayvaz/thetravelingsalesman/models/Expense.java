package com.appers.ayvaz.thetravelingsalesman.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Expense implements Parcelable{

    private int id , trip_id, client_id;

    private String type, amount, date_from,
            date_to, description;
    private byte[] image;


    public Expense(int id) {
        this.id = id;
    }

    public Expense(){}

    public int getId(){ return id;}

    public void setTrip_id(int trip_id){this.trip_id = trip_id;}

    public int getTrip_id(){return trip_id;}

    public void setClient_id(int client_id){this.client_id = client_id;}

    public int getClient_id(){return client_id;}

    public void setType(String type){this.type = type;}

    public String getType(){return type;}

    public void setAmount(String amount){this.amount = amount;}

    public String getAmount(){return amount;}

    public void setDate_from(String date_from){this.date_from = date_from;}

    public String getDate_from(){return date_from;}

    public void setDate_to(String date_to){this.date_to = date_to;}

    public String getDate_to(){return date_to;}

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
        dest.writeInt(trip_id);
        dest.writeInt(client_id);
        dest.writeString(type);
        dest.writeString(amount);
        dest.writeString(date_from);
        dest.writeString(date_to);
        dest.writeString(description);
        dest.writeByteArray(image);

    }

    public static final Parcelable.Creator<Expense> CREATOR = new Parcelable.Creator<Expense>() {
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    private Expense(Parcel in) {
        this.id = in.readInt();
        this.trip_id=in.readInt();
        this.client_id = in.readInt();
        this.type = in.readString();
        this.amount = in.readString();
        this.date_from = in.readString();
        this.date_to = in.readString();
        this.description = in.readString();
        this.image = in.createByteArray();

    }


}
