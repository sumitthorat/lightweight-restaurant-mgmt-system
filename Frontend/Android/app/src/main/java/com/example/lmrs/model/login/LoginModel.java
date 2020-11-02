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

    public boolean attemptLogin(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPasswordHash(password);

        apiInterface.loginRequest(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.i(TAG, response.body().getMessage());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i(TAG, "Could not connect! : " + t.getMessage());
            }
        });

//        LoginResponse loginResponse = null;
//        try {
//            loginResponse = apiInterface.loginRequest(loginRequest).execute().body();
//        } catch (IOException e) {
//            Log.e(TAG, e.getMessage());
//        }
//        return loginResponse != null && loginResponse.getStatus() != -1;

        return true;
    }
}
