package com.zeinab.weatherapp.weather.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zeinab.weatherapp.HomeFragment
import com.zeinab.weatherapp.R
import com.zeinab.weatherapp.model.Hourly
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HoursRecyclerAdapter:
    RecyclerView.Adapter<HoursRecyclerAdapter.ViewHolder>() {
    var hours: List<Hourly> = ArrayList<Hourly>()
    lateinit var context: Context
    fun setHourlyData(_context: Context,hours: List<Hourly>){
        context= _context
        this.hours = hours
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hours_layout,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var sdf = SimpleDateFormat("hh a")
        var dateFormate= Date(hours[position].dt!!*1000)
        var hourOfDay = sdf.format(dateFormate)


        if(HomeFragment.sharedPreferences.getString("weatherTemperature", "")=="Celsius"
            || HomeFragment.sharedPreferences.getString("weatherTemperature", "")=="" ) {
            var c=(hours[position].temp!!-273.15).toInt()
            holder.hourTemp.text="${c.toString()} ℃"
        }
        else if(HomeFragment.sharedPreferences.getString("weatherTemperature", "")=="Fahrenheit"){
            var c=(((hours[position].temp!!-273.15) * (9 / 5)) + 32).toInt()
            holder.hourTemp.text="${c.toString()} ℉"


        }
        else if(HomeFragment.sharedPreferences.getString("weatherTemperature", "")=="Kelvin"){
            var c=hours[position].temp!!
            holder.hourTemp.text="${c.toString()} K"
        }

        holder.hourTime.text=hourOfDay
        var myIcon = "https://openweathermap.org/img/w/${hours[position].weather[0].icon}.png"
        Glide.with(context).load(myIcon)
            .placeholder(R.drawable.cloud)
            .error(R.drawable.cloud)
            .into(holder.pmOrAmImg)
    }

    override fun getItemCount(): Int  = hours?.size!!
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pmOrAmImg: ImageView = itemView.findViewById(R.id.pmOrAmImg)
        var hourTime: TextView = itemView.findViewById(R.id.hourTime)
        var hourTemp: TextView = itemView.findViewById(R.id.hourTemp)

       // var layout: CardView = itemView.findViewById(R.id.hourCardView)
    }

}