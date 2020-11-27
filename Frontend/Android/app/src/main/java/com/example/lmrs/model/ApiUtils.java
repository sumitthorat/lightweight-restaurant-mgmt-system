package com.example.lmrs.model;

/**
 * Class to instantiate Api Interface user
 */
public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://10.0.2.2:5000/"; // For emulator

    public static ApiInterface getApiInterface() {

        return ApiClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
