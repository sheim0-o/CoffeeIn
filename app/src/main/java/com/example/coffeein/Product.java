package com.example.coffeein;

import android.support.annotation.Nullable;

import java.util.Date;

public final class Product {
    private int id;
    private String name;
    private String structure;
    private Date shelfLife;
    private String storageConditions;
    private String image;
    private int price;
    private int amount;

    public Product(int id, String name, String structure, Date shelfLife, String storageConditions, String image, int price, int amount) {
        this.id = id;
        this.name = name;
        this.structure = structure;
        this.shelfLife = shelfLife;
        this.storageConditions = storageConditions;
        this.image = image;
        this.price = price;
        this.amount = amount;
    }

    public Product(int id, String name, String structure, Date shelfLife, String storageConditions, String image, int price) {
        this.id = id;
        this.name = name;
        this.structure = structure;
        this.shelfLife = shelfLife;
        this.storageConditions = storageConditions;
        this.image = image;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public Date getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Date shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getStorageConditions() {
        return storageConditions;
    }

    public void setStorageConditions(String storageConditions) {
        this.storageConditions = storageConditions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}