package com.zeinab.weatherapp.model

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//alert
data class Alerts(


    var sender_name: String? = null,
    var event: String? = null,
    var start: Long? = null,
    var end: Long? = null,
    var description: String? = null,
    var tags: ArrayList<String> = arrayListOf(),

    )

//current
data class Current(


    var dt: Int? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null,
    var temp: Double? = null,
    var feels_like: Double? = null,
    var pressure: Int? = null,
    var humidity: Int? = null,
    var dew_point: Double? = null,
    var uvi: Double? = null,
    var clouds: Int? = null,
    var visibility: Int? = null,
    var wind_speed: Double? = null,
    var wind_deg: Int? = null,
    var weather: ArrayList<Weather> = arrayListOf(),

    )

//daily
data class Daily(

    var dt: Long? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null,
    var moonrise: Int? = null,
    var moonset: Int? = null,
    var moon_phase: Double? = null,
    var temp: Temp? = Temp(),
    var feels_like: FeelsLike? = FeelsLike(),
    var pressure: Int? = null,
    var humidity: Int? = null,
    var dew_point: Double? = null,
    var wind_speed: Double? = null,
    var wind_deg: Int? = null,
    var wind_gust: Double? = null,
    var weather: ArrayList<Weather> = arrayListOf(),
    var clouds: Int? = null,
    var pop: Double? = null,
    var uvi: Double? = null,

    )

//FeelsLike
data class FeelsLike(

    var day: Double? = null,
    var night: Double? = null,
    var eve: Double? = null,
    var morn: Double? = null,

    )



//hourly
data class Hourly(

    var dt: Long? = null,
    var temp: Double? = null,
    var feels_like: Double? = null,
    var pressure: Int? = null,
    var humidity: Int? = null,
    var dew_point: Double? = null,
    var uvi: Double? = null,
    var clouds: Int? = null,
    var visibility: Int? = null,
    var wind_speed: Double? = null,
    var wind_deg: Int? = null,
    var wind_gust: Double? = null,
    var weather: ArrayList<Weather> = arrayListOf(),
    var pop: Double? = null,

    )

//minutly
data class Minutely(

    @SerializedName("dt") var dt: Int? = null,
    @SerializedName("precipitation") var precipitation: Int? = null,

    )

//ResponseModel
@Entity(tableName = "weatherData")
data class ResponseModel(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id:Int,
    var lat: Double? = null,
    var lon: Double? = null,
    var timezone: String? = null,
    var timezone_offset: Int? = null,
    @Embedded
    var current: Current? = Current(),
   // var minutely: ArrayList<Minutely> = arrayListOf(),
    var hourly: ArrayList<Hourly> = arrayListOf(),
    var daily: ArrayList<Daily> = arrayListOf(),
    var alerts: ArrayList<Alerts> = arrayListOf()
    )

//temp
data class Temp(

    var day: Double? = null,
    var min: Double? = null,
    var max: Double? = null,
    var night: Double? = null,
    var eve: Double? = null,
    var morn: Double? = null,

    )

//Weather
data class Weather(

    var id: Int? = null,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null,

    )
@Entity(tableName = "favourite_weather")
data class FavWeather(
    var lat: Double? = null,
    var lon: Double? = null,

    @PrimaryKey
    @NonNull
    var city_name:String

)
@Entity(tableName = "alert_weather")
data class AlertWeather(
    var lat: Double? = null,
    var lon: Double? = null,
    var startDate:String?=null,
    var endDate:String?=null,
    var starTime:String?=null,
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int

)