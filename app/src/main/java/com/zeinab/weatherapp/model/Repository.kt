package com.zeinab.weatherapp.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.zeinab.weatherapp.database.LocalSource

import com.zeinab.weatherapp.network.IRemoteSource

class Repository private constructor(
    var remoteSource: IRemoteSource,
    var localSource: LocalSource,
    var context: Context
) : IRepository {
    companion object{
        private var instance: Repository? = null
        fun getInstance(remoteSource: IRemoteSource,
                        localSource:LocalSource,
                        context: Context): Repository{
            return instance?: Repository(
                remoteSource,
                localSource,
                context)
        }
    }

    override suspend fun getAllWeatherData(lat:Double,lon:Double,appKey:String): ResponseModel {
       return remoteSource.getWeatherFromNetwork(lat,lon,appKey)
    }

    override val storedWeather: LiveData<ResponseModel>
        get() = localSource.getAllStoredWeatherInfo

    override fun inserWeather(weather: ResponseModel) {
       localSource.insertWeatherInfo(weather)
    }


    override val storedFavWeather: LiveData<List<FavWeather>>
        get() = localSource.getAllStoredFavWeather
      //get() = TODO("Not yet implemented")

    override fun inserFavWeather(favWeather: FavWeather) {
        localSource.inserFavtWeather(favWeather)
        //TODO("Not yet implemented")
    }

    override fun deleteFavWeather(favWeather: FavWeather) {
        localSource.deleteFavWeather(favWeather)
       // TODO("Not yet implemented")
    }


}