package com.example.lmrs.model;

import com.example.lmrs.model.login.LoginRequest;
import com.example.lmrs.model.login.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface ApiInterface {

    @Headers("Content-Type: application/json")
    @PUT("login")
    Call<LoginResponse> loginRequest(@Body LoginRequest loginRequest);
}
