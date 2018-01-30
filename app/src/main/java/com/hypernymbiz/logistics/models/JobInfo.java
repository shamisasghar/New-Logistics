package com.hypernymbiz.logistics.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JobInfo {

    @SerializedName("assigned_device")
    @Expose
    private String assignedDevice;
    @SerializedName("assigned_device_id")
    @Expose
    private Integer assignedDeviceId;
    @SerializedName("notification_id")
    @Expose
    private Integer notificationId;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("job_name")
    @Expose
    private String jobName;
    @SerializedName("job_status")
    @Expose
    private String jobStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("created_datetime")
    @Expose
    private String createdDatetime;
    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("is_viewed")
    @Expose
    private List<IsViewed> isViewed = null;

    public String getAssignedDevice() {
        return assignedDevice;
    }

    public void setAssignedDevice(String assignedDevice) {
        this.assignedDevice = assignedDevice;
    }

    public Integer getAssignedDeviceId() {
        return assignedDeviceId;
    }

    public void setAssignedDeviceId(Integer assignedDeviceId) {
        this.assignedDeviceId = assignedDeviceId;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public List<IsViewed> getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(List<IsViewed> isViewed) {
        this.isViewed = isViewed;
    }

}