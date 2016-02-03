package com.appers.ayvaz.thetravelingsalesman.models;


import android.content.ContentValues;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import com.appers.ayvaz.thetravelingsalesman.database.DbSchema;
import com.appers.ayvaz.thetravelingsalesman.utils.CommUtils;

import java.util.Locale;
import java.util.UUID;

public class Client {
    private final UUID id;
    private String firstName, lastName, email, company, firstPhone, secondPhone,
            designation, note, group, address, linkedIn;

    public static final String LINKEDIN_DOMAIN = "https://www.linkedin.com/";

    private boolean stared;

    public Client() {
        this(UUID.randomUUID());

    }

    @Override
    public String toString() {
        if (!TextUtils.isEmpty(firstName) || !TextUtils.isEmpty(lastName)) {
            return firstName +
                    (TextUtils.isEmpty(firstName) ? "" : " ")
                    + lastName;
        }

        if (!TextUtils.isEmpty(firstPhone)) {
            return getFirstPhone();
        }

        if (!TextUtils.isEmpty(secondPhone)) {
            return getSecondPhone();
        }

        return email;

    }

    private boolean isEmpty(String s) {
        return s == null || s.equals("");
    }


    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public boolean isStared() {
        return stared;
    }

    public void setStared(boolean stared) {
        this.stared = stared;
    }

    public Client(UUID id) {
        this.id = id;
    }

    public UUID getId() {return id;};

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;

    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFirstPhone() {
//        return firstPhone;
        return TextUtils.isEmpty(firstPhone) ? "" : PhoneNumberUtils.formatNumber(firstPhone);
    }

    public String getSecondPhone() {
        return TextUtils.isEmpty(secondPhone) ? "" : PhoneNumberUtils.formatNumber(secondPhone);
    }

    public void setSecondPhone(String phone) {
        this.secondPhone = CommUtils.normalizeNumber(phone);
    }

    public void setFirstPhone(String mobile) {
        this.firstPhone = CommUtils.normalizeNumber(mobile);
    }

    public String getLinkedInFull() {
        return LINKEDIN_DOMAIN + getLinkedIn();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /*public void setText(View view) {
        TextView tv = (TextView) view.findViewById(R.id.firstName);
        tv.setText(firstName);
        tv = (TextView) view.findViewById(R.id.lastName);
        tv.setText(lastName);
        tv = (TextView) view.findViewById(R.id.clientPhone);
        tv.setText(email);
    }*/

    public String getPhotoFileName(boolean temp) {
        return "IMG_" + getId().toString() +
                (temp ? "_tmp" : "") + ".jpg";
    }

    public String getSecondRow() {
        String dash = "";
        if (!isEmpty(company) && (!isEmpty(firstPhone) || !isEmpty(secondPhone))) {
            dash = " - ";
        }

        return company + dash + (isEmpty(getFirstPhone()) ? getSecondPhone() : getFirstPhone());
    }

    public String getFullName() {
        String space = (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)) ?
                " " : "";

        String result = firstName + space + lastName;
        if (TextUtils.isEmpty(result)) {
            return "(Empty Name)";
        } else {
            return result;
        }
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DbSchema.ClientTable.Cols.UUID, id.toString());
        values.put(DbSchema.ClientTable.Cols.LAST_NAME, lastName);
        values.put(DbSchema.ClientTable.Cols.FIRST_NAME, firstName);
        values.put(DbSchema.ClientTable.Cols.EMAIL, email);
        values.put(DbSchema.ClientTable.Cols.ADDRESS, address);
        values.put(DbSchema.ClientTable.Cols.COMPANY, getCompany());
        values.put(DbSchema.ClientTable.Cols.NOTE, getNote());
        values.put(DbSchema.ClientTable.Cols.FIRST_PHONE, firstPhone);
        values.put(DbSchema.ClientTable.Cols.SECOND_PHONE, secondPhone);
        values.put(DbSchema.ClientTable.Cols.STARED, isStared() ? 1 : 0);
        values.put(DbSchema.ClientTable.Cols.LINKEDIN, getLinkedIn());
//        values.put(ClientTable.Cols.IMAGE, client.getImage());

        return values;
    }
}
