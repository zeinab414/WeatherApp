package com.zeinab.weatherapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zeinab.weatherapp.database.ConcreteLocalSourceClass
import com.zeinab.weatherapp.favourite.view.MyFavAdapter
import com.zeinab.weatherapp.favourite.view.OnClickDeleteListner
import com.zeinab.weatherapp.favourite.viewmodel.FavViewModel
import com.zeinab.weatherapp.favourite.viewmodel.FavViewModelFactory
import com.zeinab.weatherapp.model.FavWeather
import com.zeinab.weatherapp.model.Repository
import com.zeinab.weatherapp.network.WeatherClient
import com.zeinab.weatherapp.weather.view.DaysRecyclerAdapter
import com.zeinab.weatherapp.weather.view.HoursRecyclerAdapter


class FavouriteFragment : Fragment(),OnClickDeleteListner{
    private lateinit var iCommunicator: ICommunicator
    private lateinit var favViewModel: FavViewModel
    private lateinit var favViewModelFactory: FavViewModelFactory
    private lateinit var favWeather: FavWeather

    lateinit var favRecyclerView: RecyclerView
    lateinit var favRecyclerAdapter: MyFavAdapter
    lateinit var favLinearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var v:View=inflater.inflate(R.layout.fragment_favourite, container, false)
        var addToFavBtn: FloatingActionButton=v.findViewById(R.id.addToFavBtn)
        addToFavBtn.setOnClickListener {
            HomeActivity.goToAntherFragmement=2
            val i = Intent(requireContext(), MyMapActivity::class.java)
            startActivity(i)
        }
        iCommunicator=activity as ICommunicator
        favRecyclerView=v.findViewById(R.id.favRecyclerView)
        favLinearLayoutManager=LinearLayoutManager(requireContext())
        favLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        favRecyclerView.setLayoutManager(favLinearLayoutManager)

        favRecyclerAdapter= MyFavAdapter(this,iCommunicator)
        favRecyclerAdapter.notifyDataSetChanged()
        favRecyclerView.setAdapter(favRecyclerAdapter)
        favViewModelFactory= FavViewModelFactory(Repository.getInstance(WeatherClient.getInstance(),
            ConcreteLocalSourceClass(requireContext()),requireContext()),
           0.0, 0.0,"0406f3883d8b6a4d0cdf992646df99a0")
        favViewModel= ViewModelProvider(this,favViewModelFactory).get(FavViewModel::class.java)

        favViewModel.getAllFavWeather().observe(requireActivity()) { favWeather ->

            if (favWeather != null)
                favRecyclerAdapter.setFavWeather(requireContext(),favWeather)
                favRecyclerAdapter.notifyDataSetChanged()
        }


        return v
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouriteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onClick(weather: FavWeather) {
       favViewModel.removeFromFav(weather)
        Toast.makeText(requireContext(),"Deleted",Toast.LENGTH_LONG).show()
    }
}