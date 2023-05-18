package com.capstone.smart_white_cane.server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public API api;
    //TODO: IP 넣어주기
    private final String url = "211.36.133.46:8080/roadway/";

    public RetrofitBuilder() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);
    }
}
