package com.example.lmrs.model;

import com.example.lmrs.model.editmenu.AddItemJSONRequest;
import com.example.lmrs.model.editmenu.AddItemJSONResponse;
import com.example.lmrs.model.editmenu.GetCategoriesJSONResponse;
import com.example.lmrs.model.editmenu.GetFullMenuJSONResponse;
import com.example.lmrs.model.login.AttemptLoginJSONRequest;
import com.example.lmrs.model.login.LoginJSONResponse;
import com.example.lmrs.model.login.NewUserJSONRequest;
import com.example.lmrs.model.vieworders.GetPendingOrdersResponseJSON;
import com.example.lmrs.model.vieworders.OrderCompleteJSONRequest;
import com.example.lmrs.model.vieworders.OrderCompleteJSONResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @GET("GetCategories")
    Call<GetCategoriesJSONResponse> getAllCategories();

    @GET("GetFullMenu")
    Call<GetFullMenuJSONResponse> getFullMenu();

    @PUT("AddItemToMenu")
    Call<AddItemJSONResponse> addItemToMenu(@Body AddItemJSONRequest addItemJSONRequest);

    @GET("GetPendingOrders")
    Call<List<GetPendingOrdersResponseJSON>> getPendingOrders();

    @PUT("OrderComplete")
    Call<OrderCompleteJSONResponse> notifyOrderComplete(@Body OrderCompleteJSONRequest orderCompleteJSONRequest);

}
