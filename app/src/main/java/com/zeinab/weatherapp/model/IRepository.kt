package com.zeinab.weatherapp.model

import androidx.lifecycle.LiveData

interface IRepository {
    //from network
    suspend fun getAllWeatherData(lat:Double,lon:Double,appKey:String):ResponseModel



    //room-stored movies
    val storedWeather: LiveData<List<ResponseModel>>
    fun inserWeather(weather: ResponseModel)
    fun deleteWeather(weather: ResponseModel)

}