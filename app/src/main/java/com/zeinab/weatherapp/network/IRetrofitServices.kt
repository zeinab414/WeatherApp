package com.zeinab.weatherapp.network

import com.zeinab.weatherapp.model.ResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface IRetrofitServices {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") appID:String,
        @Query("lang")lang:String
    ):ResponseModel

}