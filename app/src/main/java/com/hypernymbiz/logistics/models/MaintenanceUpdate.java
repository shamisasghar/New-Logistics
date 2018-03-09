package com.hypernymbiz.logistics.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Metis on 09-Mar-18.
 */

public class MaintenanceUpdate {

    @SerializedName("OperationSuccessful")
    @Expose
    private Boolean operationSuccessful;

    public Boolean getOperationSuccessful() {
        return operationSuccessful;
    }

    public void setOperationSuccessful(Boolean operationSuccessful) {
        this.operationSuccessful = operationSuccessful;
    }

}