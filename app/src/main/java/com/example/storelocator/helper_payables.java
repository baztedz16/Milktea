package com.example.storelocator;

public class helper_payables {
    String date_topay,reference_no,status,rider,amount,txntype;

    public helper_payables(String date_topay,String reference_no,String status,String rider,String amount,String txntype){
        this.date_topay = date_topay;
        this.reference_no = reference_no;
        this.status = status;
        this.rider = rider;
        this.amount = amount;
        this.txntype=txntype;
    }
    public  helper_payables(){

    }

    public String getDate_topay() {
        return date_topay;
    }

    public void setDate_topay(String date_topay) {
        this.date_topay = date_topay;
    }

    public String getReference_no() {
        return reference_no;
    }

    public void setReference_no(String reference_no) {
        this.reference_no = reference_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTxntype() {
        return txntype;
    }

    public void setTxntype(String txntype) {
        this.txntype = txntype;
    }
}
