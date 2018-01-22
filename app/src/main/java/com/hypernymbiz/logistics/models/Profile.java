package com.hypernymbiz.logistics.models;

/**
  Created by shamis on 08-Jan-18.**/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {


        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("customer")
        @Expose
        private String customer;
        @SerializedName("age")
        @Expose
        private Object age;
        @SerializedName("cnic")
        @Expose
        private Object cnic;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("assignments")
        @Expose
        private Integer assignments;
        @SerializedName("date_of_joining")
        @Expose
        private String dateOfJoining;
        @SerializedName("salary")
        @Expose
        private Object salary;
        @SerializedName("marital_status")
        @Expose
        private String maritalStatus;
        @SerializedName("photo")
        @Expose
        private String photo;
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
        private Object endDatetime;

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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Object getAge() {
        return age;
    }

    public void setAge(Object age) {
        this.age = age;
    }

    public Object getCnic() {
        return cnic;
    }

    public void setCnic(Object cnic) {
        this.cnic = cnic;
    }

    public Object getDob() {
        return dob;
    }

    public void setDob(Object dob) {
        this.dob = dob;
    }

    public Integer getAssignments() {
        return assignments;
    }

    public void setAssignments(Integer assignments) {
        this.assignments = assignments;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public Object getSalary() {
        return salary;
    }

    public void setSalary(Object salary) {
        this.salary = salary;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public Object getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Object endDatetime) {
        this.endDatetime = endDatetime;
    }


}
