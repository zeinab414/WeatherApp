package com.zeinab.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zeinab.weatherapp.alert.view.MyAlertAdapter
import com.zeinab.weatherapp.alert.view.OnClickDeleteListnerAlert
import com.zeinab.weatherapp.alert.viewmodel.AlertViewModel
import com.zeinab.weatherapp.alert.viewmodel.AlertViewModelFactory
import com.zeinab.weatherapp.database.ConcreteLocalSourceClass
import com.zeinab.weatherapp.favourite.view.MyFavAdapter
import com.zeinab.weatherapp.favourite.viewmodel.FavViewModel
import com.zeinab.weatherapp.favourite.viewmodel.FavViewModelFactory
import com.zeinab.weatherapp.model.AlertWeather
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.Repository
import com.zeinab.weatherapp.network.WeatherClient


class AlertFragment : Fragment(),OnClickDeleteListnerAlert {
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var alertViewModelFactory: AlertViewModelFactory
    private lateinit var alertWeather: AlertWeather

    lateinit var alertRecyclerView: RecyclerView
    lateinit var alertRecyclerAdapter: MyAlertAdapter
    lateinit var alertLinearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var v:View=inflater.inflate(R.layout.fragment_alert, container, false)



        var addAlertBtn: FloatingActionButton =v.findViewById(R.id.addAlertBtn)

        addAlertBtn.setOnClickListener {


            var navController:NavController=Navigation.findNavController(addAlertBtn)
            var navDirections:NavDirections=AlertFragmentDirections.alertMakealertDest()
            navController.navigate(navDirections)

        }

        alertRecyclerView=v.findViewById(R.id.alertRecyclerView)
        alertLinearLayoutManager=LinearLayoutManager(requireContext())
        alertLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        alertRecyclerView.setLayoutManager(alertLinearLayoutManager)

        alertRecyclerAdapter= MyAlertAdapter(this)
        alertRecyclerAdapter.notifyDataSetChanged()
        alertRecyclerView.setAdapter(alertRecyclerAdapter)
        alertViewModelFactory= AlertViewModelFactory(Repository.getInstance(WeatherClient.getInstance(),
            ConcreteLocalSourceClass(requireContext()),requireContext()),
            0.0, 0.0,"0406f3883d8b6a4d0cdf992646df99a0")
        alertViewModel= ViewModelProvider(this,alertViewModelFactory).get(AlertViewModel::class.java)

        alertViewModel.getAllAlertWeather().observe(requireActivity()) { alertWeather ->

            if (alertWeather != null)
                alertRecyclerAdapter.setFavWeather(requireContext(),alertWeather)
            alertRecyclerAdapter.notifyDataSetChanged()
        }


        return v
    }

    override fun onClick(weather: AlertWeather) {
        alertViewModel.removeFromAlert(weather)
        Toast.makeText(requireContext(),"Deleted", Toast.LENGTH_LONG).show()
    }


}