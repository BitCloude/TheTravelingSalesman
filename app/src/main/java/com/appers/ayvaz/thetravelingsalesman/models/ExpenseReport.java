package com.appers.ayvaz.thetravelingsalesman.models;

import java.util.Calendar;

/**
 * Created by D on 017 02 17.
 */
public class ExpenseReport {

    private Calendar date;

    public Calendar getToDate() {
        return toDate;
    }

    public void setToDate(Calendar toDate) {
        this.toDate = toDate;
    }

    private Calendar toDate;

    private String info;
    private double hotel;
    private double cabs;
    private double gifts;
    private double other;
    private Client client;


    public static final String TOTAL = "Total";

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ExpenseReport() {
        hotel = 0;
        cabs = 0;
        gifts = 0;
        other = 0;
        restaurant = 0;
    }

    public double getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(double restaurant) {
        this.restaurant = restaurant;
    }

    public void addRestaurant(double amount) { this.restaurant += amount;}


    private double restaurant;

    public double getCabs() {
        return cabs;
    }

    public void setCabs(double cabs) {
        this.cabs = cabs;
    }
    public void addCabs(double amount) { this.cabs += amount;}

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public double getGifts() {
        return gifts;
    }


    public void setGifts(double gifts) {
        this.gifts = gifts;
    }
    public void addGifts(double amount) { this.gifts += amount;}


    public double getHotel() {
        return hotel;
    }
    public void addHotel(double amount) { this.hotel += amount;}


    public void setHotel(double hotel) {
        this.hotel = hotel;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getOther() {
        return other;
    }
    public void addOther(double amount) { this.other += amount;}


    public void setOther(double other) {
        this.other = other;
    }

    public double getTotal() {
        return hotel + restaurant + cabs + gifts + other;
    }


}


