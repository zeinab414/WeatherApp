package com.zeinab.weatherapp.network

import com.zeinab.weatherapp.model.ResponseModel

interface IRemoteSource {
    suspend fun getWeatherFromNetwork(lat:Double,lon:Double,appKey:String,lang:String):ResponseModel
}