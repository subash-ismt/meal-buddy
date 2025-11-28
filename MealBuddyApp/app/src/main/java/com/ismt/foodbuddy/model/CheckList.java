package com.ismt.foodbuddy.model;

public class CheckList {

    private String itemName;
    private String availableLocation;

    public CheckList(String itemName, String availableLocation) {
        this.itemName = itemName;
        this.availableLocation = availableLocation;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAvailableLocation() {
        return availableLocation;
    }
    public void setAvailableLocation(String availableLocation) {
        this.availableLocation = availableLocation;
    }
}
