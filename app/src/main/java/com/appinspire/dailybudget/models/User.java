package com.appinspire.dailybudget.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shamis on 29-Dec-17.
 */

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("preferred_module")
    @Expose
    private Integer preferredModule;
    @SerializedName("associated_entity")
    @Expose
    private Integer associatedEntity;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user_role")
    @Expose
    private String userRole;
    @SerializedName("module")
    @Expose
    private List<Module> module = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPreferredModule() {
        return preferredModule;
    }

    public void setPreferredModule(Integer preferredModule) {
        this.preferredModule = preferredModule;
    }

    public Integer getAssociatedEntity() {
        return associatedEntity;
    }

    public void setAssociatedEntity(Integer associatedEntity) {
        this.associatedEntity = associatedEntity;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public List<Module> getModule() {
        return module;
    }

    public void setModule(List<Module> module) {
        this.module = module;
    }

}