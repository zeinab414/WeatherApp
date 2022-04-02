package com.zeinab.weatherapp.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//alert
data class Alerts(


    var sender_name: String? = null,
    var event: String? = null,
    var start: Int? = null,
    var end: Int? = null,
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
//@Entity(tableName = "weather")
data class ResponseModel(
    var lat: Double? = null,
    var lon: Double? = null,
    var latLong:String="$lat$lon",
    var fav:Boolean=false,
    var timezone: String? = null,
    var timezone_offset: Int? = null,
    var current: Current? = Current(),
    var minutely: ArrayList<Minutely> = arrayListOf(),
    var hourly: ArrayList<Hourly> = arrayListOf(),
    var daily: ArrayList<Daily> = arrayListOf(),
    var alerts: ArrayList<Alerts> = arrayListOf()
    //@PrimaryKey
   // @NonNull


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