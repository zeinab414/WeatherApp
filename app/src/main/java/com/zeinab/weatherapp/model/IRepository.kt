package com.zeinab.weatherapp.model

import androidx.lifecycle.LiveData

interface IRepository {
    //from network
    suspend fun getAllWeatherData(lat:Double,lon:Double,appKey:String,lang:String):ResponseModel



    //room-stored Weather
    val storedWeather: LiveData<ResponseModel>
    fun inserWeather(weather: ResponseModel)


    //fav
    val storedFavWeather: LiveData<List<FavWeather>>
    fun inserFavWeather(favWeather: FavWeather)
    fun deleteFavWeather(favWeather: FavWeather)

    //alert
    val storedAlertWeather: LiveData<List<AlertWeather>>
    fun inserAlertWeather(alertWeather: AlertWeather)
    fun deleteAlertWeather(alertWeather: AlertWeather)

}