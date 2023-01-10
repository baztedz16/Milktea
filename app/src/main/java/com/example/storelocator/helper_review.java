package com.example.storelocator;

public class helper_review {
    String orderid,user,comment,ratingtype,rating_count,order_date,store;


    public helper_review(String orderid, String user, String comment, String ratingtype, String rating_count, String order_date,String store) {
        this.orderid = orderid;
        this.user = user;
        this.comment = comment;
        this.ratingtype = ratingtype;
        this.rating_count = rating_count;
        this.order_date = order_date;
        this.store=store;
    }
    public helper_review(){

    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRatingtype() {
        return ratingtype;
    }

    public void setRatingtype(String ratingtype) {
        this.ratingtype = ratingtype;
    }

    public String getRating_count() {
        return rating_count;
    }

    public void setRating_count(String rating_count) {
        this.rating_count = rating_count;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
