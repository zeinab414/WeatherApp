package com.zeinab.weatherapp

import android.content.Context
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.util.Log


class InternetConnection(var contex:Context) {

        fun checkConnection():Boolean{
            var connected = false
            val cm= contex!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo =cm.activeNetworkInfo

            if (networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected.also {
                    connected = it
                }) {

                return connected
            }
            return connected


    }
}