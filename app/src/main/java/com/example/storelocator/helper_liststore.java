package com.example.storelocator;

public class helper_liststore {
    String accountype,address,email,fullname,password,phone,storename,username,image,destlat,destlong,currLocation,metric;

    public helper_liststore(String accountype, String address, String email, String fullname, String password, String phone,
                            String storename, String username,String image,String destlat,String destlong,String currLocation,String metric) {
        this.accountype = accountype;
        this.address = address;
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.phone = phone;
        this.storename = storename;
        this.username = username;
        this.image = image;
        this.destlat = destlat;
        this.destlong = destlong;
        this.currLocation = currLocation;
        this.metric = metric;
    }
    public  helper_liststore(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAccountype() {
        return accountype;
    }

    public void setAccountype(String accountype) {
        this.accountype = accountype;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDestlat() {
        return destlat;
    }

    public void setDestlat(String destlat) {
        this.destlat = destlat;
    }

    public String getDestlong() {
        return destlong;
    }

    public void setDestlong(String destlong) {
        this.destlong = destlong;
    }

    public String getCurrLocation() {
        return currLocation;
    }

    public void setCurrLocation(String currLocation) {
        this.currLocation = currLocation;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }
}
