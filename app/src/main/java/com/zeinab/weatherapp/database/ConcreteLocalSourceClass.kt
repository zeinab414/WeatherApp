package com.zeinab.weatherapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.zeinab.weatherapp.model.AlertWeather
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.ResponseModel


class ConcreteLocalSourceClass(context: Context):LocalSource {
    private val dao: MyWeatherDAO?
    override val getAllStoredFavWeather: LiveData<List<FavWeather>>
    override val getAllStoredWeatherInfo: LiveData<ResponseModel>
    override val getAllStoredAlertWeather: LiveData<List<AlertWeather>>



    init {
        val  db:DatabaseApp= DatabaseApp.getInstance(context)
        dao=db.weatherDAO()
        getAllStoredFavWeather=dao.favWeather
        getAllStoredWeatherInfo=dao.weatherInfo
        getAllStoredAlertWeather=dao.alertWeather
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

    override fun inserAlerttWeather(weatherItem: AlertWeather) {
        dao?.insertAllAlert(weatherItem)
    }

    override fun deleteAlertWeather(weatherItem: AlertWeather) {
        dao?.deleteAlert(weatherItem)
    }
}