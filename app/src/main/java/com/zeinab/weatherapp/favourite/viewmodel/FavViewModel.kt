package com.zeinab.weatherapp.favourite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.IRepository
import com.zeinab.weatherapp.model.ResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavViewModel(iRepo: IRepository, var lat:Double, var lon:Double, var appKey:String): ViewModel()  {
    private val _iRepo: IRepository = iRepo


     fun getAllFavWeather():LiveData<List<FavWeather>> {

            return _iRepo.storedFavWeather

    }

    fun removeFromFav(favWeather: FavWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            _iRepo.deleteFavWeather(favWeather)
        }
    }

        fun insertFavWeather(favWeather: FavWeather) {
        viewModelScope.launch(Dispatchers.IO){
            _iRepo.inserFavWeather(favWeather)
        }
    }

    override fun onCleared() {
        super.onCleared()

    }

}