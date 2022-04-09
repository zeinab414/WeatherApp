package com.zeinab.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zeinab.weatherapp.model.AlertWeather
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.ResponseModel

@Database(entities = [ResponseModel::class,FavWeather::class,AlertWeather::class],version = 6)
@TypeConverters(TypeConverterToRoom::class)
abstract class DatabaseApp: RoomDatabase() {
    abstract fun weatherDAO(): MyWeatherDAO
    companion object {
        private var INSTANCE: DatabaseApp? = null
        //one thread at a time to access this method
        @Synchronized
        fun getInstance(context: Context): DatabaseApp{
            return INSTANCE?: Room.databaseBuilder(
                context.applicationContext,
                DatabaseApp::class.java,
                "favourite_weather"
            ).fallbackToDestructiveMigration().build()
        }
    }
}