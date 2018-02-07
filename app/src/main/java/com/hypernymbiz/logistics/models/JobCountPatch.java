package com.hypernymbiz.logistics.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Metis on 07-Feb-18.
 */

public class JobCountPatch {


    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
