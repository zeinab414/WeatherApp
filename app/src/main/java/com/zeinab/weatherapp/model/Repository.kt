package com.zeinab.weatherapp.model

import android.content.Context
import androidx.lifecycle.LiveData

import com.zeinab.weatherapp.network.IRemoteSource

class Repository private constructor(
    var remoteSource: IRemoteSource,

    var context: Context
) : IRepository {
    companion object{
        private var instance: Repository? = null
        fun getInstance(remoteSource: IRemoteSource,

                        context: Context): Repository{
            return instance?: Repository(
                remoteSource, context)
        }
    }

    override suspend fun getAllWeatherData(lat:Double,lon:Double,appKey:String): ResponseModel {
       return remoteSource.getWeatherFromNetwork(lat,lon,appKey)
    }

    override val storedWeather: LiveData<List<ResponseModel>>
        get() = TODO("Not yet implemented")

    override fun inserWeather(weather: ResponseModel) {
        TODO("Not yet implemented")
    }

    override fun deleteWeather(weather: ResponseModel) {
        TODO("Not yet implemented")
    }


}