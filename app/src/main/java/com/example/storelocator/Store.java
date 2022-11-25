package com.example.storelocator;

public class Store {
    private String image;
    private String storename;
    private String address;
    private String location;

    public Store(String image, String storename, String address, String location) {
        this.image = image;
        this.storename = storename;
        this.address = address;
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public String getStorename() {
        return storename;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }
}

