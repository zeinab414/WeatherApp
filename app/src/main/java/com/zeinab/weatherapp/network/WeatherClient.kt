package com.zeinab.weatherapp.network

import com.zeinab.weatherapp.model.ResponseModel

class WeatherClient private constructor():IRemoteSource {
    override suspend fun getWeatherFromNetwork(lat:Double,lon:Double,appKey:String): ResponseModel {
        val retrofitService = RetrofitHelper.getClient()?.create(IRetrofitServices::class.java)
        var response = retrofitService?.getWeather(lat,lon,appKey)

        return response!!
    }
    companion object{
        private var instance:WeatherClient? = null
        fun getInstance():WeatherClient{
            return instance?: WeatherClient()
        }
    }
}