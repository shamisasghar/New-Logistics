package com.hypernymbiz.logistics.models;

import java.util.List;

public class JobInfo {

    private String assignedDevice;

    private Integer assignedDeviceId;

    private Integer notificationId;

    private Integer jobId;

    private String jobName;

    private String jobStatus;

    private String status;

    private String customerName;

    private Integer customerId;
    private String createdDatetime;
    private String createdTime;
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