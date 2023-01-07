package com.example.storelocator;

public class helper_category {
    String categoryname,cateogoryid,store;

    public helper_category(String categoryname, String cateogoryid, String store) {
        this.categoryname = categoryname;
        this.cateogoryid = cateogoryid;
        this.store = store;
    }

    public helper_category(){

    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCateogoryid() {
        return cateogoryid;
    }

    public void setCateogoryid(String cateogoryid) {
        this.cateogoryid = cateogoryid;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
