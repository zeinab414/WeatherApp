package com.zeinab.weatherapp

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zeinab.weatherapp.database.ConcreteLocalSourceClass
import com.zeinab.weatherapp.model.Repository
import com.zeinab.weatherapp.network.WeatherClient
import com.zeinab.weatherapp.weather.view.DaysRecyclerAdapter
import com.zeinab.weatherapp.weather.view.HoursRecyclerAdapter
import com.zeinab.weatherapp.weather.viewmodel.ViewModelFactory
import com.zeinab.weatherapp.weather.viewmodel.WeatherViewModel
import java.util.*

class ShowFavInfoFragment : Fragment() {
    lateinit var viewModel: WeatherViewModel
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var hourFavRecyclerView: RecyclerView
    lateinit var hoursFavRecyclerAdapter: HoursRecyclerAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var daysFavRecyclerView: RecyclerView
    lateinit var daysFavRecyclerAdapter: DaysRecyclerAdapter
    lateinit var daysFavLinearLayoutManager: LinearLayoutManager

    lateinit var txtFavDate: TextView
    lateinit var txtFavTime: TextView
    lateinit var txtFavGovernate: TextView
    lateinit var txtFavCloudStatus: TextView
    lateinit var txFavtCurrentTemp: TextView

    lateinit var txtFavPressure: TextView
    lateinit var txtFavHumidity: TextView
    lateinit var txtFavWind: TextView
    lateinit var txtFavVisibility: TextView
    lateinit var txtFavCloud: TextView
    lateinit var txtFavUltra: TextView

    lateinit var cloudFavImg: ImageView

    var latitude: Double = 0.0
    var longtitude: Double = 0.0

    lateinit var internetConnection: InternetConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var v:View=inflater.inflate(R.layout.fragment_show_fav_info, container, false)
        internetConnection = InternetConnection(requireContext())
        latitude= arguments!!.getDouble("lat")
        longtitude= arguments!!.getDouble("lon")

        txtFavDate = v.findViewById(R.id.txtFavDate)
        txtFavTime = v.findViewById(R.id.txtFavTime)
        txtFavCloudStatus = v.findViewById(R.id.txtFavCloudStatus)
        txFavtCurrentTemp = v.findViewById(R.id.txFavtCurrentTemp)
        txtFavGovernate = v.findViewById(R.id.txtFavGovernate)

        txtFavPressure = v.findViewById(R.id.txtFavPressure)
        txtFavHumidity = v.findViewById(R.id.txtFavHumidity)
        txtFavWind = v.findViewById(R.id.txtFavWind)
        txtFavVisibility = v.findViewById(R.id.txtFavVisibility)
        txtFavCloud = v.findViewById(R.id.txtFavCloud)
        txtFavUltra = v.findViewById(R.id.txtFavUltra)

        cloudFavImg = v.findViewById(R.id.cloudFavImg)

        hourFavRecyclerView = v.findViewById(R.id.hourFavRecyclerView)
        linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL)
        hourFavRecyclerView.setLayoutManager(linearLayoutManager)

        hoursFavRecyclerAdapter = HoursRecyclerAdapter()
        hoursFavRecyclerAdapter.notifyDataSetChanged()
        hourFavRecyclerView.setAdapter(hoursFavRecyclerAdapter)

        daysFavRecyclerView = v.findViewById(R.id.daysFavRecyclerView)
        daysFavLinearLayoutManager = LinearLayoutManager(requireContext())
        daysFavLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        daysFavRecyclerView.setLayoutManager(daysFavLinearLayoutManager)

        daysFavRecyclerAdapter = DaysRecyclerAdapter()
        daysFavRecyclerAdapter.notifyDataSetChanged()
        daysFavRecyclerView.setAdapter(daysFavRecyclerAdapter)

        citydData()
        return v
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
        txtFavGovernate.text = getCityName(latitude, longtitude)
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
                txtFavDate.text = allDate
                txtFavTime.text = allTime
                var c = (weathers[0].current?.temp!! - 273.15).toInt()
                txFavtCurrentTemp.text = "${c.toString()} \u2103"
                txtFavCloudStatus.text =
                    "${weathers[0].current?.weather?.get(0)?.description.toString()}"

                txtFavPressure.text = "${weathers[0].current?.pressure.toString()} hpa"
                txtFavHumidity.text = "${weathers[0].current?.humidity.toString()} %"
                txtFavWind.text = "${weathers[0].current?.wind_speed.toString()} m/s"
                txtFavCloud.text = "${weathers[0].current?.clouds.toString()} %"
                txtFavUltra.text = "${weathers[0].current?.uvi.toString()} "
                txtFavVisibility.text = "${weathers[0].current?.visibility.toString()} m"
                var myIcon =
                    "https://openweathermap.org/img/w/${weathers[0].current?.weather?.get(0)?.icon}.png"

                Glide.with(requireContext()).load(myIcon)
                    .placeholder(R.drawable.cloud)
                    .error(R.drawable.cloud)
                    .into(cloudFavImg)
                if (weathers != null) {
                    hoursFavRecyclerAdapter.setHourlyData(requireContext(), weathers[0].hourly)
                    daysFavRecyclerAdapter.setDailyData(requireContext(), weathers[0].daily)

                    Log.i("saved",""+viewModel.getWeather().value)
                }
            }
        }
        else{}

    }

}