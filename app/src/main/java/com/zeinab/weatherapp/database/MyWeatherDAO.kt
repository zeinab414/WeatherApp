package com.zeinab.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zeinab.weatherapp.model.AlertWeather
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.ResponseModel

@Dao
interface MyWeatherDAO {
    @get:Query("SELECT * From favourite_weather")
    val favWeather: LiveData<List<FavWeather>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(favWeather: FavWeather)

    @Delete
    fun delete(favWeather: FavWeather)




    //alert
    @get:Query("SELECT * From alert_weather")
    val alertWeather: LiveData<List<AlertWeather>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllAlert(alertWeather: AlertWeather)
    @Delete
    fun deleteAlert(alertWeather: AlertWeather)
}