package com.zeinab.weatherapp

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zeinab.weatherapp.model.ResponseModel
import com.zeinab.weatherapp.network.WeatherClient
import kotlinx.coroutines.runBlocking


class AlertNotificationWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params)
{
    lateinit var notificationAlertManager: NotificationAlertManager


    lateinit var responseModel: ResponseModel

    override suspend fun doWork(): Result {

        val data = inputData
        val latitude = data.getDouble("lat",0.0)
        val longitude = data.getDouble("lon",0.0)
        val client: WeatherClient = WeatherClient.getInstance()
        runBlocking {
            responseModel = client.getWeatherFromNetwork(latitude,longitude,"0406f3883d8b6a4d0cdf992646df99a0",
                HomeFragment.sharedPreferences.getString("weatherLanguage","en")!!)
        }
        val content = responseModel.current!!.temp.toString()

        if(responseModel.alerts!=null){
            notificationAlertManager = NotificationAlertManager(applicationContext)
            val nb: NotificationCompat.Builder = notificationAlertManager.getChannelNotification(
                "Weather Alerts", responseModel.alerts[0].description )!!
            notificationAlertManager.getManager().notify(1, nb.build())

        }
        else{
            notificationAlertManager = NotificationAlertManager(applicationContext)
            val nb: NotificationCompat.Builder = notificationAlertManager.getChannelNotification(
                "Weather Alerts","No Alert!!" )!!
            notificationAlertManager.getManager().notify(1, nb.build())
        }

        Log.i("Date", "inDoWork")


        return Result.success()
    }

}