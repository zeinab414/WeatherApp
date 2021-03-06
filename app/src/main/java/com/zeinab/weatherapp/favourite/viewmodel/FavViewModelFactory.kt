package com.zeinab.weatherapp.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zeinab.weatherapp.model.IRepository


class FavViewModelFactory (private val _irepo: IRepository, private val lat:Double, private val lon:Double, private val appKey:String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            FavViewModel(_irepo, lat, lon, appKey) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}