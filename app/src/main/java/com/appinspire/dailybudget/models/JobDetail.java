package com.appinspire.dailybudget.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Metis on 11-Jan-18.
 */
public class JobDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("modified_by")
    @Expose
    private String modifiedBy;
    @SerializedName("created_datetime")
    @Expose
    private String createdDatetime;
    @SerializedName("modified_datetime")
    @Expose
    private Object modifiedDatetime;
    @SerializedName("end_datetime")
    @Expose
    private String endDatetime;
    @SerializedName("assignments")
    @Expose
    private Integer assignments;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("job_start_datetime")
    @Expose
    private String jobStartDatetime;
    @SerializedName("job_end_datetime")
    @Expose
    private String jobEndDatetime;
    @SerializedName("job_status")
    @Expose
    private String jobStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Object getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Object modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Integer getAssignments() {
        return assignments;
    }

    public void setAssignments(Integer assignments) {
        this.assignments = assignments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobStartDatetime() {
        return jobStartDatetime;
    }

    public void setJobStartDatetime(String jobStartDatetime) {
        this.jobStartDatetime = jobStartDatetime;
    }

    public String getJobEndDatetime() {
        return jobEndDatetime;
    }

    public void setJobEndDatetime(String jobEndDatetime) {
        this.jobEndDatetime = jobEndDatetime;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

}