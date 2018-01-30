package com.hypernymbiz.logistics.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IsViewed {


    @SerializedName("viewed")
    @Expose
    private Boolean viewed;
    @SerializedName("email")
    @Expose
    private String email;
    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
