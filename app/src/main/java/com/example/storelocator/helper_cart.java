package com.example.storelocator;

public class helper_cart {
    String img,itmname,owner,product,username,delete,cartid,qty,orderstatus,price,order_id,itemrating,size;

    public helper_cart(String img,String itmname,String owner,String product,String username,String delete,String cartid,String qty,
                       String orderstatus,String price,String order_id,String itemrating,String size){
        this.img = img;
        this.itmname = itmname;
        this.owner = owner;
        this.product = product;
        this.username = username;
        this.delete = delete;
        this.cartid = cartid;
        this.qty =qty;
        this.orderstatus=orderstatus;
        this.price = price;
        this.order_id = order_id;
        this.itemrating = itemrating;
        this.size = size;



    }
    public  helper_cart(){

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getItmname() {
        return itmname;
    }

    public void setItmname(String itmname) {
        this.itmname = itmname;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getCartid() {
        return cartid;
    }

    public void setCartid(String cartid) {
        this.cartid = cartid;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getItemrating() {
        return itemrating;
    }

    public void setItemrating(String itemrating) {
        this.itemrating = itemrating;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
