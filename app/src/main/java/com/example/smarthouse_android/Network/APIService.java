package com.example.smarthouse_android.Network;

import com.example.smarthouse_android.Model.DeviceModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    @POST("")
    Call<String> checkLogin();

    @PUT("Rest_smarthouse_war_exploded/api/devices/device/{key}/{updateValue}")
    Call<DeviceModel> update(@Path("key") String key, @Path("updateValue") String value);

    @GET("Rest_smarthouse_war_exploded/api/devices")
    Call<DeviceModel> getDevices();



}
