package com.example.lmrs.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewUserJSONRequest {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password_hash")
    @Expose
    private String passwordHash;
    @SerializedName("role")
    @Expose
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
