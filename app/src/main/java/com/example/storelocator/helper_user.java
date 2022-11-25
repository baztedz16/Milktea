package com.example.storelocator;

public class helper_user {
    String fullname,username,password,email,storename,phone,accountype,destlong,destlat,image,view,activation,Address;
    public helper_user() {
    }
    public helper_user(String fullname, String username, String password, String email, String storename, String phone,String accountype,String destlong,String destlat,String image,String view,String activation,String Address) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.storename = storename;
        this.phone = phone;
        this.accountype = accountype;
        this.destlong = destlong;
        this.destlat = destlat;
        this.image = image;
        this.view = view;
        this.activation = activation;
        this.Address = Address;
    }



    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccountype() {
        return accountype;
    }

    public void setAccountype(String accountype) {
        this.accountype = accountype;
    }

    public String getDestlong() {
        return destlong;
    }

    public void setDestlong(String destlong) {
        this.destlong = destlong;
    }

    public String getDestlat() {
        return destlat;
    }

    public void setDestlat(String destlat) {
        this.destlat = destlat;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
