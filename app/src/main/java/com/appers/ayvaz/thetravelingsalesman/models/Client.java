package com.appers.ayvaz.thetravelingsalesman.models;


import java.util.UUID;

public class Client {
    private final UUID id;
    private String firstName, lastName, email, company, firstPhone, secondPhone,
            designation, note, group, address, linkedIn;

    private boolean stared;

    public Client() {
        this(UUID.randomUUID());

    }

    @Override
    public String toString() {
        if (!isEmpty(firstName) || !isEmpty(lastName)) {
            return firstName +
                    (isEmpty(firstName) ? "" : " ")
                    + lastName;
        }

        if (!isEmpty(firstPhone)) {
            return firstPhone;
        }

        if (!isEmpty(secondPhone)) {
            return secondPhone;
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
        return firstPhone;
    }

    public String getSecondPhone() {
        return secondPhone;
    }

    public void setSecondPhone(String phone) {
        this.secondPhone = phone;
    }

    public void setFirstPhone(String mobile) {
        this.firstPhone = mobile;
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

        return company + dash + (isEmpty(firstPhone) ? secondPhone : firstPhone);
    }
}
