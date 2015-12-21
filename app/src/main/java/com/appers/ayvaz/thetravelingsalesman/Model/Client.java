package com.appers.ayvaz.thetravelingsalesman.Model;


import android.view.View;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.R;

public class Client {
    private final int id;
    private String firstName, lastName, email, company, mobile, designation, note, group;

    public Client(int id) {
        this.id = id;
    }

    public int getId() {return id;};

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public void setText(View view) {
        TextView tv = (TextView) view.findViewById(R.id.firstName);
        tv.setText(firstName);
        tv = (TextView) view.findViewById(R.id.lastName);
        tv.setText(lastName);
        tv = (TextView) view.findViewById(R.id.email);
        tv.setText(email);
    }
}
