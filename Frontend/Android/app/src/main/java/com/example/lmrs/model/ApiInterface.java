package com.example.lmrs.model;

import com.example.lmrs.model.login.AttemptLoginJSONRequest;
import com.example.lmrs.model.login.LoginJSONResponse;
import com.example.lmrs.model.login.NewUserJSONRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface ApiInterface {

    // API calls interface for LoginModel

    @Headers("Content-Type: application/json")
    @PUT("AttemptLogin")
    Call<LoginJSONResponse> attemptLogin(@Body AttemptLoginJSONRequest attemptLoginJSONRequest);

    @Headers("Content-Type: application/json")
    @PUT("AddNewUser")
    Call<LoginJSONResponse> addNewUser(@Body NewUserJSONRequest newUserJSONRequest);
}
