package com.capstone.smart_white_cane.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public API api;
    private final String url = "124.59.46.235/roadway/";
    private Gson gson = new GsonBuilder().setLenient().create();

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    public RetrofitBuilder() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        api = retrofit.create(API.class);
    }
}
