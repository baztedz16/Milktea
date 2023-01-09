package com.example.storelocator;

public class helper_product {
    String paroductName,storeOwner,productImage,address,itemID,destlong,destlat,storeUser,Description,price,pricesm,pricemd,pricelg,category;
    int productview;

    public helper_product(String paroductName, String storeOwner, String productImage,String address,String itemID,String destlat,String destlong,String storeUser,int productview,String Description,String price,String pricesm,String pricemd,String pricelg,String category) {
        this.paroductName = paroductName;
        this.storeOwner = storeOwner;
        this.productImage = productImage;
        this.address = address;
        this.itemID = itemID;
        this.destlong = destlong;
        this.destlat = destlat;
        this.storeUser = storeUser;
        this.productview = productview;
        this.Description = Description;
        this.price = price;
        this.pricesm = pricesm;
        this.pricemd = pricemd;
        this.pricelg = pricelg;
        this.category=category;

    }

    public helper_product() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParoductName() {
        return paroductName;
    }

    public void setParoductName(String paroductName) {
        this.paroductName = paroductName;
    }

    public String getStoreOwner() {
        return storeOwner;
    }

    public void setStoreOwner(String storeOwner) {
        this.storeOwner = storeOwner;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemID() {
        return itemID;
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


    public String getStoreUser() {
        return storeUser;
    }

    public int getProductview() {
        return productview;
    }

    public void setProductview(int productview) {
        this.productview = productview;
    }

    public void setStoreUser(String storeUser) {
        this.storeUser = storeUser;

    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricesm() {
        return pricesm;
    }

    public void setPricesm(String pricesm) {
        this.pricesm = pricesm;
    }

    public String getPricemd() {
        return pricemd;
    }

    public void setPricemd(String pricemd) {
        this.pricemd = pricemd;
    }

    public String getPricelg() {
        return pricelg;
    }

    public void setPricelg(String pricelg) {
        this.pricelg = pricelg;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
