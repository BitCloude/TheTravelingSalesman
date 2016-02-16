package com.appers.ayvaz.thetravelingsalesman.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;
import java.util.UUID;

public class Expense implements Parcelable{

    private int id = -1;
    private int trip_id;
    private UUID client_id;

    private String type, amount, description, imageFile;
    private Calendar date_from, date_to;


    public Expense(int id) {
        this.id = id;
    }

    public Expense(){}

    public String toString() {
        if (!isEmpty(type) || !isEmpty(amount)) {
            return type+
                    (isEmpty(type) ? "" : " ")
                    + amount;
        }

            return description;
    }

    private boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    public int getId(){ return id;}

    public void setId(int id){this.id = id;}

    public void setTrip_id(int trip_id){this.trip_id = trip_id;}

    public int getTrip_id(){return trip_id;}

    public void setClient_id(UUID client_id){this.client_id = client_id;}

    public UUID getClient_id(){return client_id;}

    public void setType(String type){this.type = type;}

    public String getType(){return type;}

    public void setAmount(String amount){this.amount = amount;}

    public String getAmount(){return amount;}

    public void setDate_from(Calendar date_from){this.date_from = date_from;}

    public Calendar getDate_from(){return date_from;}

    public void setDate_to(Calendar date_to){this.date_to = date_to;}

    public Calendar getDate_to(){return date_to;}

    public void setDescription(String description){this.description = description;}

    public String getDescription(){return description;}

    public void setImageFile(String imageFile){this.imageFile = imageFile;}

    public String getImageFile(){return imageFile;}

    public String getPhotoFileName(boolean temp) {
        return "IMG_EXPENSE_" + Integer.toString(getId()) +
                (temp ? "_tmp" : "") + ".jpg";
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(trip_id);
        dest.writeSerializable(client_id);
        dest.writeString(type);
        dest.writeString(amount);
        dest.writeSerializable(date_from);
        dest.writeSerializable(date_to);
        dest.writeString(description);
        dest.writeString(imageFile);

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
        this.client_id = (UUID) in.readSerializable();
        this.type = in.readString();
        this.amount = in.readString();
        this.date_from = (Calendar) in.readSerializable();
        this.date_to = (Calendar) in.readSerializable();
        this.description = in.readString();
        this.imageFile = in.readString();

    }


}
