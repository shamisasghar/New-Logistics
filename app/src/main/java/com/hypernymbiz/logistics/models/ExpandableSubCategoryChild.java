package com.hypernymbiz.logistics.models;

/**
 * Created by Metis on 20-Jan-18.
 */

public class ExpandableSubCategoryChild {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String name;
    public String time;

    public ExpandableSubCategoryChild(String name,String time) {
        this.name = name;
        this.time=time;
    }
}
