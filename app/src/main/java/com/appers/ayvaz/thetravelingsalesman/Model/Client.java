package com.appers.ayvaz.thetravelingsalesman.Model;

/**
 * Created by D on 12/09/2015.
 */
public class Client {
    public int id;
    public String getLastName() {
        return lastName;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    private String firstName, lastName, email, company;

    public Client(int id, String firstName, String lastName, String email, String company) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.company = company;
    }
}
