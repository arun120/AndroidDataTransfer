package com.example.home.offlinetransfer;

import com.google.gson.Gson;

/**
 * Created by Fluffy on 23-10-2017.
 */
public class TransactionDetails {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String ifsc;
    private String branch;
    private String bank;
    private String acnumber;
    private String type;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAcnumber() {
        return acnumber;
    }

    public void setAcnumber(String acnumber) {
        this.acnumber = acnumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIfsc() {

        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getJson(){

        return new Gson().toJson(this);
    }

    public static TransactionDetails fromJson(String json){

        return new Gson().fromJson(json,TransactionDetails.class);

    }

    @Override
    public String toString() {
        return name;
    }
}
