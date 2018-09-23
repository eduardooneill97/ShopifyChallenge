package com.example.eduar.shopifychallenge;

import java.util.ArrayList;
import java.util.List;

public class Product {

    private int id;
    private String name;
    private int inventoryLvl;

    public Product(int id, String name){
        this.id = id;
        this.name = name;
        this.inventoryLvl = 0;
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

    public int getInventoryLvl() {
        return inventoryLvl;
    }

    public void setInventoryLvl(int inventoryLvl) {
        this.inventoryLvl = inventoryLvl;
    }
}
