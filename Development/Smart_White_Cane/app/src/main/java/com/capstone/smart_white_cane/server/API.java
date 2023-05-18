package com.capstone.smart_white_cane.server;


import com.capstone.smart_white_cane.map.data.RoadAddress;
import com.capstone.smart_white_cane.server.dto.RouteDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {
    @GET("test")
    public Call<String> getTestResponse();
    @GET("checkscore")
    public Call<Integer> getScoreResponse(@Body RouteDto routeDto);
    @POST()
    public void setScoreResponse(@Body RoadAddress roadAddress, Integer score);
    @GET("detect")
    public Call<String> detect(@Body String encodedBitmap);

}
