package com.appinspire.dailybudget.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shamis on 29-Dec-17.
 */


public class Module {

    @SerializedName("module_name")
    @Expose
    private String moduleName;
    @SerializedName("module_id")
    @Expose
    private Integer moduleId;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

}
