package com.hypernymbiz.logistics.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Metis on 16-Jan-18.
 */

public class StartJob {
    @SerializedName("job_name")
    @Expose
    private String jobName;
    @SerializedName("job_actual_start_time")
    @Expose
    private String jobActualStartTime;
    @SerializedName("job_actual_end_time")
    @Expose
    private String jobActualEndTime;
    @SerializedName("job_start_lat")
    @Expose
    private Double jobStartLat;
    @SerializedName("job_start_lng")
    @Expose
    private Double jobStartLng;
    @SerializedName("job_end_lat")
    @Expose
    private Double jobEndLat;
    @SerializedName("job_end_lng")
    @Expose
    private Double jobEndLng;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobActualStartTime() {
        return jobActualStartTime;
    }

    public void setJobActualStartTime(String jobActualStartTime) {
        this.jobActualStartTime = jobActualStartTime;
    }

    public String getJobActualEndTime() {
        return jobActualEndTime;
    }

    public void setJobActualEndTime(String jobActualEndTime) {
        this.jobActualEndTime = jobActualEndTime;
    }

    public Double getJobStartLat() {
        return jobStartLat;
    }

    public void setJobStartLat(Double jobStartLat) {
        this.jobStartLat = jobStartLat;
    }

    public Double getJobStartLng() {
        return jobStartLng;
    }

    public void setJobStartLng(Double jobStartLng) {
        this.jobStartLng = jobStartLng;
    }

    public Double getJobEndLat() {
        return jobEndLat;
    }

    public void setJobEndLat(Double jobEndLat) {
        this.jobEndLat = jobEndLat;
    }

    public Double getJobEndLng() {
        return jobEndLng;
    }

    public void setJobEndLng(Double jobEndLng) {
        this.jobEndLng = jobEndLng;
    }
}