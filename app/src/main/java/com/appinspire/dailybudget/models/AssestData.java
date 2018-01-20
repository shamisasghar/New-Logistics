package com.appinspire.dailybudget.models;

/**
 * Created by Shamis on 17-Dec-17.
 */

public class AssestData {


    private String itemname;
    private String itemtype;

    public AssestData(String itemname,String itemtype) {
        this.itemname = itemname;
        this.itemtype=itemtype;

    }
    public String getItemname() {
        return itemname;
    }

    public void setItemname(String name) {
        this.itemname = name;
    }

}
