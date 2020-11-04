package com.example.lmrs.model.login;

import android.util.Log;

import com.example.lmrs.model.ApiInterface;
import com.example.lmrs.model.ApiUtils;

public class LoginModel {
    ApiInterface apiInterface;

    final String TAG = "LoginModel";

    public LoginModel() {
        apiInterface = ApiUtils.getApiInterface();
    }

    public boolean attemptLogin(String username, String password, String[] err) {
        AttemptLoginJSONRequest attemptLoginJSONRequest = new AttemptLoginJSONRequest();
        attemptLoginJSONRequest.setUsername(username);
        attemptLoginJSONRequest.setPasswordHash(password);

        LoginJSONResponse loginJSONResponse = null;
        try {
            loginJSONResponse = apiInterface.attemptLogin(attemptLoginJSONRequest).execute().body();
            assert loginJSONResponse != null;
            if (loginJSONResponse.getStatus() == -1) {
                err[0] = loginJSONResponse.getMessage();
            }
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);

        }

        return loginJSONResponse != null && loginJSONResponse.getStatus() != -1;
    }

    public boolean addNewUser(String username, String password, String role, String[] err) {
        NewUserJSONRequest newUserJSONRequest = new NewUserJSONRequest();
        newUserJSONRequest.setUsername(username);
        newUserJSONRequest.setPasswordHash(password);
        newUserJSONRequest.setRole(role);

        LoginJSONResponse loginJSONResponse = null;

        try {
            loginJSONResponse = apiInterface.addNewUser(newUserJSONRequest).execute().body();
            assert loginJSONResponse != null;
            if (loginJSONResponse.getStatus() == -1) {
                err[0] = loginJSONResponse.getMessage();
            }
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);

        }

        return loginJSONResponse != null && loginJSONResponse.getStatus() != -1;
    }
}
