package com.example.lmrs.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * POJO for Attempt Login API Request
 */
public class AttemptLoginJSONRequest {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password_hash")
    @Expose
    private String passwordHash;

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

}
