package com.zeinab.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*

import com.zeinab.weatherapp.model.Repository
import com.zeinab.weatherapp.network.WeatherClient
import com.zeinab.weatherapp.weather.viewmodel.ViewModelFactory
import com.zeinab.weatherapp.weather.viewmodel.WeatherViewModel
import java.util.*


class HomeFragment : Fragment() {
    lateinit var viewModel:WeatherViewModel
    lateinit var viewModelFactory:ViewModelFactory
    lateinit var dialog: Dialog
    lateinit var radioGroup: RadioGroup
    lateinit var rbGps: RadioButton
    lateinit var rbMap: RadioButton
    var PERMISSION_ID=200
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    var latitude: Double = 0.0
    var longtitude: Double = 0.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())



        //dialog
        dialog= Dialog(requireContext())
        locationDialog()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var v:View =inflater.inflate(R.layout.fragment_home, container, false)


        return v
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    private fun locationDialog() {
        dialog.setContentView(R.layout.dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var btnOk: Button =dialog.findViewById(R.id.btnOK)
        btnOk.setOnClickListener {
            dialog.dismiss()
            viewModelFactory = ViewModelFactory(Repository.getInstance(
                WeatherClient.getInstance(),
                requireContext()
            ), latitude!!, longtitude!!,"0406f3883d8b6a4d0cdf992646df99a0")
            viewModel =ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)
            viewModel.getAllWeather()
            //Observe exposed data of viewModel
            viewModel.onlineWeather.observe(this) { weathers ->
                Log.i("TAG", "onChanged: ")
                Log.i("TAG", "onCreate: ${weathers.toString()}" )

            }
        }
        radioGroup=dialog.findViewById(R.id.radioGroup)
        rbGps=dialog.findViewById(R.id.rbGPS)
        rbMap=dialog.findViewById(R.id.rbMap)
        rbGps.setOnClickListener {
            // Toast.makeText(this,rbGps.text.toString(),Toast.LENGTH_LONG).show()
            getLastLocation()

        }
        rbMap.setOnClickListener {
            // Toast.makeText(this,"Map",Toast.LENGTH_LONG).show()
            val i = Intent(requireContext(), GoogleMapsActivity::class.java)
            startActivity(i)
        }

        dialog.show()
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)==
                PackageManager.PERMISSION_GRANTED
    }
    fun RequestPermission(){

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    @SuppressLint("MissingPermission")
    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,
            Looper.myLooper()!!
        )
    }



    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            latitude=lastLocation.latitude
            longtitude=lastLocation.longitude
            Toast.makeText(requireContext(),"You Last Location is : Long: "+ longtitude + " , Lat: " + latitude + "," + getCityName(lastLocation.latitude,lastLocation.longitude),
                Toast.LENGTH_LONG).show()


        }
    }
    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)

        cityName = Adress.get(0).locality
        countryName = Adress.get(0).countryName

        return cityName
    }
    fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location: Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        latitude=location.latitude
                        longtitude=location.longitude
                        Toast.makeText(requireContext(),"You Current Location is : Long: "+longtitude + " , Lat: " + latitude + "\n" + getCityName(location.latitude,location.longitude),
                            Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)


            }
        }else{
            RequestPermission()
        }
    }


}