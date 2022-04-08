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
import java.lang.String.format
import android.text.format.DateFormat




class MakeAlertFragment : Fragment() {
lateinit var txtStartTimeAlert:TextView
lateinit var txtStartDateAlert:TextView
lateinit var txtEndTimeAlert:TextView
lateinit var txtEndDateAlert:TextView
lateinit var btnChooseLocation:Button
lateinit var btnSaveAlert:Button

    var hour:Int = 0
    var minute:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
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
            setAlertDate(txtStartDateAlert)
        }
        txtEndDateAlert.setOnClickListener{
            setAlertDate(txtEndDateAlert)
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

            var navController: NavController = Navigation.findNavController(btnSaveAlert)
            var navDirections: NavDirections =MakeAlertFragmentDirections.makealertAlertDest()
            navController.navigate(navDirections)
        }
        return v
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MakeAlertFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setAlertDate(textView:TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            context!!, { datePicker, year, month, day ->
                var month = month
                month = month + 1
                var date2 = "$day/$month/$year"
                textView.setText(date2)
            }, year, month, day
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