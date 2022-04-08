package com.zeinab.weatherapp.database

import androidx.lifecycle.LiveData
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.ResponseModel


interface LocalSource {
    fun inserFavtWeather(weatherItem: FavWeather)
    fun deleteFavWeather(weatherItem: FavWeather)
    val getAllStoredFavWeather: LiveData<List<FavWeather>>

    //weather info
    fun insertWeatherInfo(weatherItem: ResponseModel)
    val getAllStoredWeatherInfo: LiveData<ResponseModel>
}