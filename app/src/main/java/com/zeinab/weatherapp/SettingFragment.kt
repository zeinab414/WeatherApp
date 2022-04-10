package com.zeinab.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.*


class SettingFragment : Fragment() {
 lateinit var rgNotification:RadioGroup
 lateinit var rgTemp:RadioGroup
 lateinit var rgWind:RadioGroup
 lateinit var rgLanguage:RadioGroup
 lateinit var rgLocation:RadioGroup

 lateinit var radioGPSLocation:RadioButton
 lateinit var radioMapLocation:RadioButton
 lateinit var radioEnglishLanguage:RadioButton
 lateinit var radioArabicLanguage:RadioButton
 lateinit var radioCelsius:RadioButton
 lateinit var radioKelvin:RadioButton
 lateinit var radioFahrenheit:RadioButton
 lateinit var radioMeter:RadioButton
 lateinit var radioMill:RadioButton
 lateinit var radioEnable:RadioButton
 lateinit var radioDisable:RadioButton

    var PERMISSION_ID = 200
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    var latitude: Double = 0.0
    var longtitude: Double = 0.0
    var weatherTemperature=""
     var weatherVis=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
       var v:View=inflater.inflate(R.layout.fragment_setting, container, false)
        rgNotification=v.findViewById(R.id.rgNotification)
        rgTemp=v.findViewById(R.id.rgTemp)
        rgWind=v.findViewById(R.id.rgWind)
        rgLanguage=v.findViewById(R.id.rgLanguage)
        rgLocation=v.findViewById(R.id.rgLocation)

        radioGPSLocation=v.findViewById(R.id.radioGPSLocation)
        radioMapLocation=v.findViewById(R.id.radioMapLocation)
        radioEnglishLanguage=v.findViewById(R.id.radioEnglishLanguage)
        radioArabicLanguage=v.findViewById(R.id.radioArabicLanguage)
        radioCelsius=v.findViewById(R.id.radioCelsius)
        radioKelvin=v.findViewById(R.id.radioKelvin)
        radioFahrenheit=v.findViewById(R.id.radioFahrenheit)
        radioMeter=v.findViewById(R.id.radioMeter)
        radioMill=v.findViewById(R.id.radioMill)
        radioEnable=v.findViewById(R.id.radioEnable)
        radioDisable=v.findViewById(R.id.radioDisable)

        radioEnglishLanguage.setOnClickListener {
            setLocal("en")
            loadLocal()


        }
        radioArabicLanguage.setOnClickListener {
            setLocal("ar")
            loadLocal()

        }
        radioGPSLocation.setOnClickListener {
         getLastLocation()

        }
        radioMapLocation.setOnClickListener {
            HomeActivity.goToAntherFragmement=4
            val i = Intent(requireContext(), MyMapActivity::class.java)
            startActivity(i)

        }
        radioCelsius.setOnClickListener {
          weatherTemperature="Celsius"
        }
        radioFahrenheit.setOnClickListener {

            weatherTemperature="Fahrenheit"
        }
        radioKelvin.setOnClickListener {

            weatherTemperature="Kelvin"
        }
        radioKelvin.setOnClickListener {

            weatherTemperature="Kelvin"
        }
        radioMeter.setOnClickListener {

            weatherVis="meter"
        }
        radioMill.setOnClickListener {

            weatherVis="mill"
        }
        return v
    }

    private fun setLocal(language: String) {
        var locale:Locale= Locale(language)
        Locale.setDefault(locale)
        var configuration:Configuration=Configuration()
        configuration.locale=locale
        resources.updateConfiguration(configuration,resources.displayMetrics)
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("Languages",Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("language",language)
        editor.apply()

    }
    public fun loadLocal(){
       var sharedPreferences:SharedPreferences= requireActivity().getSharedPreferences("Languages",Context.MODE_PRIVATE)
        var languages: String? =sharedPreferences.getString("language","")
        setLocal(languages!!)
    }
    fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        NewLocationData()
                    } else {
                        latitude = location.latitude
                        longtitude = location.longitude
                        Toast.makeText(requireContext(),
                            "You Current Location is : Long: " + longtitude + " , Lat: " + latitude + "\n" + getCityName(
                                location.latitude,
                                location.longitude),
                            Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)


            }
        } else {
            RequestPermission()
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }
    private fun checkPermission(): Boolean {

        return ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }
    fun RequestPermission() {

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }
    @SuppressLint("MissingPermission")
    fun NewLocationData() {
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback,
            Looper.myLooper()!!
        )
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            latitude = lastLocation.latitude
            longtitude = lastLocation.longitude
            Toast.makeText(requireContext(),
                "You Last Location is : Long: " + longtitude + " , Lat: " + latitude + "," + getCityName(
                    lastLocation.latitude,
                    lastLocation.longitude),
                Toast.LENGTH_LONG).show()




        }
    }
    private fun getCityName(lat: Double, long: Double): String {
        var cityName: String = ""
        var countryName = ""
        var geoCoder = Geocoder(requireContext(), Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat, long, 3)

        cityName = Adress.get(0).adminArea
        //     countryName = Adress.get(0).countryName

        return cityName
    }

    override fun onStop() {
        super.onStop()
        if (latitude != 0.0 && longtitude != 0.0) {
            HomeFragment.sharedPreferences.edit().putString("latitude", latitude.toString())
                .commit()
            HomeFragment.sharedPreferences.edit().putString("longtitude", longtitude.toString())
                .commit()

        }
if(weatherTemperature!="") {
    HomeFragment.sharedPreferences.edit()
        .putString("weatherTemperature", weatherTemperature).commit()
}



    if(weatherVis!="") {
        HomeFragment.sharedPreferences.edit()
            .putString("weatherVis", weatherVis).commit()
    }


}

}