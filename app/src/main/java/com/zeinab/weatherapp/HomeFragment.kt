package com.zeinab.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE

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
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.zeinab.weatherapp.database.ConcreteLocalSourceClass

import com.zeinab.weatherapp.model.Repository
import com.zeinab.weatherapp.network.WeatherClient
import com.zeinab.weatherapp.weather.view.DaysRecyclerAdapter
import com.zeinab.weatherapp.weather.view.HoursRecyclerAdapter
import com.zeinab.weatherapp.weather.viewmodel.ViewModelFactory
import com.zeinab.weatherapp.weather.viewmodel.WeatherViewModel
import java.util.*
import android.content.SharedPreferences





class HomeFragment : Fragment() {
    lateinit var viewModel: WeatherViewModel
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var dialog: Dialog
    lateinit var radioGroup: RadioGroup
    lateinit var rbGps: RadioButton
    lateinit var rbMap: RadioButton
    var PERMISSION_ID = 200
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    lateinit var hoursRecyclerView: RecyclerView
    lateinit var hoursRecyclerAdapter: HoursRecyclerAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var daysRecyclerView: RecyclerView
    lateinit var daysRecyclerAdapter: DaysRecyclerAdapter
    lateinit var daysLinearLayoutManager: LinearLayoutManager

    lateinit var txtDate: TextView
    lateinit var txtTime: TextView
    lateinit var txtGovernate: TextView
    lateinit var txtCloudStatus: TextView
    lateinit var txtCurrentTemp: TextView

    lateinit var txtPressure: TextView
    lateinit var txtHumidity: TextView
    lateinit var txtWind: TextView
    lateinit var txtVisibility: TextView
    lateinit var txtCloud: TextView
    lateinit var txtUltra: TextView

    lateinit var ultraImg: ImageView
    lateinit var pressurrImg: ImageView
    lateinit var visImg: ImageView
    lateinit var cloudImg: ImageView
    lateinit var windImg: ImageView
    lateinit var humidityImg: ImageView
    lateinit var internetConnection: InternetConnection


    var latitude: Double = 0.0
    var longtitude: Double = 0.0

    lateinit var sharedPreferences: SharedPreferences
    lateinit var sharedEditor: SharedPreferences.Editor



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        //dialog
        //dialog= Dialog(requireContext())
        //locationDialog()
        sharedPreferences = requireContext().
        getSharedPreferences("com.zeinab.weatherapp", MODE_PRIVATE)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var v: View = inflater.inflate(R.layout.fragment_home, container, false)
        //start
        internetConnection = InternetConnection(requireContext())


        //end
        txtDate = v.findViewById(R.id.txtDate)
        txtTime = v.findViewById(R.id.txtTime)
        txtCloudStatus = v.findViewById(R.id.txtCloudStatus)
        txtCurrentTemp = v.findViewById(R.id.txtCurrentTemp)
        txtGovernate = v.findViewById(R.id.txtGovernate)


        txtCloud = v.findViewById(R.id.txtCloud)
        txtHumidity = v.findViewById(R.id.txtHumidity)
        txtUltra = v.findViewById(R.id.txtUltra)
        txtVisibility = v.findViewById(R.id.txtVisibility)
        txtPressure = v.findViewById(R.id.txtPressure)
        txtWind = v.findViewById(R.id.txtWind)

        cloudImg = v.findViewById(R.id.cloudImg)
        humidityImg = v.findViewById(R.id.humidityImg)
        ultraImg = v.findViewById(R.id.ultraImg)
        visImg = v.findViewById(R.id.visImg)
        pressurrImg = v.findViewById(R.id.pressurrImg)
        windImg = v.findViewById(R.id.windImg)

        hoursRecyclerView = v.findViewById(R.id.hourRecyclerView)
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL)
        hoursRecyclerView.setLayoutManager(linearLayoutManager)

        hoursRecyclerAdapter = HoursRecyclerAdapter()
        hoursRecyclerAdapter.notifyDataSetChanged()
        hoursRecyclerView.setAdapter(hoursRecyclerAdapter)

        daysRecyclerView = v.findViewById(R.id.daysRecyclerView)
        daysLinearLayoutManager = LinearLayoutManager(requireContext())
        daysLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        daysRecyclerView.setLayoutManager(daysLinearLayoutManager)

        daysRecyclerAdapter = DaysRecyclerAdapter()
        daysRecyclerAdapter.notifyDataSetChanged()
        daysRecyclerView.setAdapter(daysRecyclerAdapter)
       if(isLocationEnabled()==false){
           val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
           startActivity(intent)
           RequestPermission()
       }
               else{
            RequestPermission()
                }

            if (internetConnection.checkConnection() == true) {

                dialog = Dialog(requireContext())
                val bundle = arguments
                if (bundle != null) {
                    latitude = bundle!!.getDouble("lat")
                    longtitude = bundle!!.getDouble("lon")
                    sharedPreferences.edit().putString("latitude", latitude.toString()).commit()
                    sharedPreferences.edit().putString("longtitude", longtitude.toString()).commit()
                   Toast.makeText(requireContext(), "" + latitude, Toast.LENGTH_LONG).show()
                citydData()
               }
               else {
                    if(sharedPreferences.getBoolean("firstOne", true)== true) {
                        locationDialog()
                    }
                    else{
                        latitude = sharedPreferences.getString("latitude", "0.0")!!.toDouble()
                        longtitude = sharedPreferences.getString("longtitude","0.0")!!.toDouble()
                        Toast.makeText(requireContext(), "" + latitude, Toast.LENGTH_LONG).show()
                           citydData()

                    }




                }
            } else {
                Toast.makeText(requireContext(), "no internet", Toast.LENGTH_LONG).show()
                citydData()
            }




        return v
    }



    private fun locationDialog() {

        dialog.setContentView(R.layout.dialog_layout)
        radioGroup = dialog.findViewById(R.id.radioGroup)
        rbGps = dialog.findViewById(R.id.rbGPS)
        rbMap = dialog.findViewById(R.id.rbMap)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var btnOk: Button = dialog.findViewById(R.id.btnOK)
        btnOk.setOnClickListener {
            dialog.dismiss()
            citydData()
        }

        rbGps.setOnClickListener {
            // Toast.makeText(this,rbGps.text.toString(),Toast.LENGTH_LONG).show()
            getLastLocation()

        }
        rbMap.setOnClickListener {
            // Toast.makeText(this,"Map",Toast.LENGTH_LONG).show()
            HomeActivity.goToAntherFragmement = 1
            val i = Intent(requireContext(), MyMapActivity::class.java)
            startActivity(i)
        }
        dialog.show()

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

    private fun citydData() {
        val c = Calendar.getInstance()
        val currentDay = c[Calendar.DAY_OF_MONTH]
        val currentMonth = c[Calendar.MONTH] + 1
        val currentYear = c[Calendar.YEAR]
        val currentHours = c[Calendar.HOUR]
        val currentMinutes = c[Calendar.MINUTE]
        val days = arrayOf("Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat")
        var dayName = days[c.get(Calendar.DAY_OF_WEEK) - 1]
        var allDate = "$dayName $currentDay/$currentMonth/$currentYear"
        var allTime = "$currentHours:$currentMinutes"
        Log.i("err", "" + latitude + ";" + longtitude)
        txtGovernate.text = getCityName(latitude, longtitude)
        viewModelFactory = ViewModelFactory(Repository.getInstance(
            WeatherClient.getInstance(),
            ConcreteLocalSourceClass(requireContext()),
            requireContext()
        ), latitude!!, longtitude!!, "0406f3883d8b6a4d0cdf992646df99a0")

        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)
        if (internetConnection.checkConnection() == true) {
            viewModel.getAllWeather()
            //Observe exposed data of viewModel
            viewModel.onlineWeather.observe(this) { weathers ->
                Log.i("TAG", "onChanged: ")
                Log.i("TAG", "onCreate: ${weathers.toString()}")
                txtDate.text = allDate
                txtTime.text = allTime
                var c = (weathers[0].current?.temp!! - 273.15).toInt()
                txtCurrentTemp.text = "${c.toString()} \u2103"
                txtCloudStatus.text =
                    "${weathers[0].current?.weather?.get(0)?.description.toString()}"

                txtPressure.text = "${weathers[0].current?.pressure.toString()} hpa"
                txtHumidity.text = "${weathers[0].current?.humidity.toString()} %"
                txtWind.text = "${weathers[0].current?.wind_speed.toString()} m/s"
                txtCloud.text = "${weathers[0].current?.clouds.toString()} %"
                txtUltra.text = "${weathers[0].current?.uvi.toString()} "
                txtVisibility.text = "${weathers[0].current?.visibility.toString()} m"
                var myIcon =
                    "https://openweathermap.org/img/w/${weathers[0].current?.weather?.get(0)?.icon}.png"

                Glide.with(requireContext()).load(myIcon)
                    .placeholder(R.drawable.cloud)
                    .error(R.drawable.cloud)
                    .into(cloudImg)
                if (weathers != null) {
                    hoursRecyclerAdapter.setHourlyData(requireContext(), weathers[0].hourly)
                    daysRecyclerAdapter.setDailyData(requireContext(), weathers[0].daily)
                   // viewModel.insertWeather(weathers[0])
                    Log.i("saved",""+viewModel.getWeather().value)
                }
            }
        }
        else{}

    }

    override fun onStop() {
        super.onStop()

        if (sharedPreferences.getBoolean("firstOne", true)) {
            sharedPreferences.edit().putString("latitude", latitude.toString()).commit()
            sharedPreferences.edit().putString("longtitude", longtitude.toString()).commit()
            sharedPreferences.edit().putBoolean("firstOne", false).commit();

        }
    }


}