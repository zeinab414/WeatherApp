package com.zeinab.weatherapp.alert.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeinab.weatherapp.model.AlertWeather
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.IRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertViewModel (iRepo: IRepository, var lat:Double, var lon:Double, var appKey:String): ViewModel()  {
    private val _iRepo: IRepository = iRepo


    fun getAllAlertWeather(): LiveData<List<AlertWeather>> {

        return _iRepo.storedAlertWeather

    }

    fun removeFromAlert(alertWeather: AlertWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            _iRepo.deleteAlertWeather(alertWeather)
        }
    }

    fun insertAlertWeather(alertWeather: AlertWeather) {
        viewModelScope.launch(Dispatchers.IO){
            _iRepo.inserAlertWeather(alertWeather)
        }
    }

    override fun onCleared() {
        super.onCleared()

    }

}