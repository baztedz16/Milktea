package com.example.storelocator;


public class helper_order_rider {
    String store,order_id,status,order_user,order_total,address,longti,lati,rider,date_order,prof_image;
    int itemcount;

    public helper_order_rider(String store, String order_id, String status, String order_user, String order_total, String address, String longti, String lati, String rider, String date_order,String prof_image,int itemcount
    ){
        this.store = store;
        this.order_id = order_id;
        this.status = status;
        this.order_user = order_user;
        this.order_total = order_total;
        this.address = address;
        this.longti = longti;
        this.lati = lati;
        this.rider = rider;
        this.date_order = date_order;
        this.prof_image = prof_image;
        this.itemcount = itemcount;

    }
    public helper_order_rider(){

    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_user() {
        return order_user;
    }

    public void setOrder_user(String order_user) {
        this.order_user = order_user;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongti() {
        return longti;
    }

    public void setLongti(String longti) {
        this.longti = longti;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }

    public String getProf_image() {
        return prof_image;
    }

    public void setProf_image(String prof_image) {
        this.prof_image = prof_image;
    }

    public int getItemcount() {
        return itemcount;
    }

    public void setItemcount(int itemcount) {
        this.itemcount = itemcount;
    }
}
