package com.zeinab.weatherapp.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.zeinab.weatherapp.model.IRepository
import com.zeinab.weatherapp.model.ResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(iRepo: IRepository,var lat:Double,var lon:Double,var appKey:String,var lang:String): ViewModel() {
    private val _iRepo: IRepository = iRepo
    private val _weatherList = MutableLiveData<List<ResponseModel>>()

    init {

        getAllWeather()
    }

    //Expose returned online Data
    val onlineWeather: LiveData<List<ResponseModel>> = _weatherList

    fun getAllWeather(){
        viewModelScope.launch{
            val weathers = _iRepo.getAllWeatherData(lat,lon,appKey,lang)
            withContext(Dispatchers.Main){

                _weatherList.postValue(listOf(weathers))
            }
        }
    }

    //add  weather
    fun insertWeather(weather: ResponseModel) {
        viewModelScope.launch(Dispatchers.IO){
            _iRepo.inserWeather(weather)
        }
    }

    fun getWeather():LiveData<ResponseModel> {
       return _iRepo.storedWeather
    }

    override fun onCleared() {
        super.onCleared()

    }

}