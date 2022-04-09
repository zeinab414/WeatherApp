package com.zeinab.weatherapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import java.util.*
import android.widget.TimePicker

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.provider.ContactsContract
import java.lang.String.format
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.zeinab.weatherapp.alert.viewmodel.AlertViewModel
import com.zeinab.weatherapp.alert.viewmodel.AlertViewModelFactory
import com.zeinab.weatherapp.database.ConcreteLocalSourceClass
import com.zeinab.weatherapp.favourite.viewmodel.FavViewModel
import com.zeinab.weatherapp.favourite.viewmodel.FavViewModelFactory
import com.zeinab.weatherapp.model.AlertWeather
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.Repository
import com.zeinab.weatherapp.network.WeatherClient
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


class MakeAlertFragment : Fragment() {
lateinit var txtStartTimeAlert:TextView
lateinit var txtStartDateAlert:TextView
lateinit var txtEndTimeAlert:TextView
lateinit var txtEndDateAlert:TextView
lateinit var btnChooseLocation:Button
lateinit var btnSaveAlert:Button

    private lateinit var alertViewModel: AlertViewModel
    private lateinit var alertViewModelFactory: AlertViewModelFactory
    private lateinit var alertWeather: AlertWeather

    var hour:Int = 0
    var minute:Int = 0
    var sDay:Int = 0
    var sMonth:Int = 0
    var sYear:Int = 0
    var eDay:Int = 0
    var eMonth:Int = 0
    var eYear:Int = 0
    lateinit var FirstDate:String
    lateinit var secondDate:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        var v:View=inflater.inflate(R.layout.fragment_make_alert, container, false)
         txtStartTimeAlert=v.findViewById(R.id.txtStartTimeAlert)
         txtStartDateAlert=v.findViewById(R.id.txtStartDateAlert)
         txtEndTimeAlert=v.findViewById(R.id.txtEndTimeAlert)
         txtEndDateAlert=v.findViewById(R.id.txtEndDateAlert)
         btnChooseLocation=v.findViewById(R.id.btnChooseLocation)
         btnSaveAlert=v.findViewById(R.id.btnSaveAlert)
        txtStartDateAlert.setOnClickListener{
            setAlertStartDate(txtStartDateAlert)
        }
        txtEndDateAlert.setOnClickListener{
            setAlertEndDate(txtEndDateAlert)
        }
        txtStartTimeAlert.setOnClickListener{
            setAlertTime(txtStartTimeAlert)
        }
        txtEndTimeAlert.setOnClickListener{
          //  setAlertTime(txtEndTimeAlert)
            txtEndTimeAlert.text=MyMapActivity.lat.toString();
        }
        btnChooseLocation.setOnClickListener {
            HomeActivity.goToAntherFragmement=3
            val i = Intent(requireContext(), MyMapActivity::class.java)
            startActivity(i)
        }
        btnSaveAlert.setOnClickListener {
            val inputData = Data.Builder()
                .putDouble("lat",MyMapActivity.lat)
                .putDouble("lon",MyMapActivity.lon)
                .build()
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            val today = Calendar.getInstance()
            var startDate = Calendar.getInstance()
            var endDate = Calendar.getInstance()
            startDate.set(sYear, sMonth, sDay, hour, minute)
            endDate.set(eYear, eMonth, eDay, hour, minute)
            var diffInMinutes = (startDate.timeInMillis - today.timeInMillis) / 60000
            // diffInMinutes =  diffInMinutes - 44640
            var d1: Date? = null
            var d2: Date? = null
            try {
                d1 = simpleDateFormat.parse(FirstDate)
                d2 = simpleDateFormat.parse(secondDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val differenceInTime = d2!!.time - d1!!.time
            val diffInDays = differenceInTime / (1000 * 60 * 60 * 24) % 365
            Log.i("Tagggg","Days: "+diffInDays)
            Log.i("Tagggg", "diff In Minutes: $diffInMinutes")
            Log.i("Tagggg", diffInMinutes.toString())
            var workRequest = OneTimeWorkRequest.Builder(AlertNotificationWorker::class.java)
                .setInitialDelay(diffInMinutes, TimeUnit.MINUTES)
                .setInputData(inputData).build()

            if (diffInMinutes > 0) {
                HomeActivity.requests.add(workRequest)
                Log.i("Tagggg", "done")
            }
            for (i in 1..diffInDays) {
                val duration = Math.abs(diffInMinutes + 1440 * i)
                workRequest = OneTimeWorkRequest.Builder(AlertNotificationWorker::class.java)
                    .setInitialDelay(duration, TimeUnit.MINUTES)
                    .setInputData(inputData).build()
                HomeActivity.requests.add(workRequest)
            }
            val name = "${MyMapActivity.lat} - ${MyMapActivity.lon}"
            WorkManager.getInstance().enqueueUniqueWork(name, ExistingWorkPolicy.REPLACE, HomeActivity.requests)

           //save in room

            alertViewModelFactory= AlertViewModelFactory(Repository.getInstance(WeatherClient.getInstance(),
                ConcreteLocalSourceClass(requireContext()),requireContext()),
                MyMapActivity.lat!!, MyMapActivity.lon!!,"0406f3883d8b6a4d0cdf992646df99a0")
            alertViewModel= ViewModelProvider(this,alertViewModelFactory).get(AlertViewModel::class.java)
            alertWeather= AlertWeather(MyMapActivity.lat, MyMapActivity.lon,FirstDate,secondDate,txtStartTimeAlert.text.toString(),0)
            alertViewModel.insertAlertWeather(alertWeather)
            Toast.makeText(requireContext(),"success", Toast.LENGTH_LONG).show()


            var navController: NavController = Navigation.findNavController(btnSaveAlert)
            var navDirections: NavDirections =MakeAlertFragmentDirections.makealertAlertDest()
            navController.navigate(navDirections)
        }
        return v
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setAlertStartDate(textView:TextView) {
        val calendar = Calendar.getInstance()
         sYear = calendar[Calendar.YEAR]
         sMonth = calendar[Calendar.MONTH]
         sDay = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            context!!, { datePicker, year, month, day ->
                var month = month
                month = month + 1
               FirstDate = "$day/$month/$year"
                textView.setText(FirstDate)
            }, sYear, sMonth, sDay
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis - 1000
        datePickerDialog.show()
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setAlertEndDate(textView:TextView) {
        val calendar = Calendar.getInstance()
         eYear = calendar[Calendar.YEAR]
         eMonth = calendar[Calendar.MONTH]
         eDay = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            context!!, { datePicker, year, month, day ->
                var month = month
                month = month + 1
                secondDate = "$day/$month/$year"
                textView.setText(secondDate)
            }, eYear, eMonth, eDay
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis - 1000
        datePickerDialog.show()
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setAlertTime(textView:TextView){

        val timePickerDialog = TimePickerDialog(context,
            { timePicker, hourOfDay, minutes ->
                 hour = hourOfDay
                minute = minutes
                val calendar = Calendar.getInstance()
                calendar[0, 0, 0, hour] = minute
                textView.setText(DateFormat.format("hh:mm aa", calendar))
            }, 12, 0, false)
        timePickerDialog.updateTime(hour, minute)
        timePickerDialog.show()

    }
}