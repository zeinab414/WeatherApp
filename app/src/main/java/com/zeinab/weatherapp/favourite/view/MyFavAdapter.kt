package com.zeinab.weatherapp.favourite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zeinab.weatherapp.R
import com.zeinab.weatherapp.model.Daily
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.weather.view.DaysRecyclerAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MyFavAdapter(private val listner:OnClickDeleteListner):
    RecyclerView.Adapter<MyFavAdapter.ViewHolder>()  {
    var favWeather: List<FavWeather> = ArrayList<FavWeather>()
    lateinit var context: Context
    fun setFavWeather(_context: Context, favWeather: List<FavWeather>){
        context= _context
        this.favWeather = favWeather
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_row,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.txtFavCountry.text=favWeather[position].city_name
        holder.deleteImg.setOnClickListener {
             listner.onClick(favWeather[position])
        }



    }

    override fun getItemCount(): Int  = favWeather?.size!!
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var deleteImg: ImageView = itemView.findViewById(R.id.deleteImg)
        var txtFavCountry: TextView = itemView.findViewById(R.id.txtFavCountry)



        // var layout: CardView = itemView.findViewById(R.id.dayCardView)
    }

}