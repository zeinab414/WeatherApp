package com.zeinab.weatherapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.ResponseModel


class ConcreteLocalSourceClass(context: Context):LocalSource {
    private val dao: MyWeatherDAO?
    override val getAllStoredFavWeather: LiveData<List<FavWeather>>
    override val getAllStoredWeatherInfo: LiveData<ResponseModel>


    init {
        val  db:DatabaseApp= DatabaseApp.getInstance(context)
        dao=db.weatherDAO()
        getAllStoredFavWeather=dao.favWeather
        getAllStoredWeatherInfo=dao.weatherInfo
    }

    override fun inserFavtWeather(weatherItem: FavWeather) {
        dao?.insertAll(weatherItem)
    }

    override fun deleteFavWeather(weatherItem: FavWeather) {
        dao?.delete(weatherItem)
    }
    override fun insertWeatherInfo(weatherItem: ResponseModel) {
        dao?.insertAllWeatherInfo(weatherItem)
    }


}