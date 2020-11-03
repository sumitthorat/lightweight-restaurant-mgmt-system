package com.example.lmrs.model.login;

import android.util.Log;

import com.example.lmrs.model.ApiInterface;
import com.example.lmrs.model.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginModel {
    ApiInterface apiInterface;

    final String TAG = "LoginModel";

    public LoginModel() {
        apiInterface = ApiUtils.getApiInterface();
    }

    public boolean attemptLogin(String username, String password, String[] err) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPasswordHash(password);

        LoginResponse loginResponse = null;
        try {
            loginResponse = apiInterface.loginRequest(loginRequest).execute().body();
            assert loginResponse != null;
            if (loginResponse.getStatus() == -1) {
                err[0] = loginResponse.getMessage();
            }
        } catch (Exception e) {
            Log.e(TAG, "Ex: ", e);

        }

        return loginResponse != null && loginResponse.getStatus() != -1;
    }
}
