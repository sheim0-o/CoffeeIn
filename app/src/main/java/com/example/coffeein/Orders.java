package com.example.coffeein;

import java.util.ArrayList;
import java.util.Date;

public class Orders {
    private int client;
    private Date dateTime;
    private ArrayList<Product> products;

    public Orders(int client, Date dateTime, ArrayList<Product> products) {
        this.client = client;
        this.dateTime = dateTime;
        this.products = products;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
