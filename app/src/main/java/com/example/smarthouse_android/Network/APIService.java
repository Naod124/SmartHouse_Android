package com.example.smarthouse_android.Network;

import com.example.smarthouse_android.Model.DeviceModel;
import com.example.smarthouse_android.Model.TempHumi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    @POST("")
    Call<String> checkLogin();

    @PUT("demo_house_war_exploded/api/devices/{type}/{id}/{value}")
    Call<DeviceModel> update(@Path("type") String key, @Path("id") String value, @Path("value") String status);

    @GET("demo_house_war_exploded/api/devices")
    Call<DeviceModel> getDevices();

    @Headers({"Accept: application/json"})
    @GET("demo_house_war_exploded/api/devices/{type}")
    Call<DeviceModel> getDevice(@Path("type") String key);


}