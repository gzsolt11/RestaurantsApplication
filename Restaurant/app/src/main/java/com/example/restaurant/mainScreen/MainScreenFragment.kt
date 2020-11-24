package com.example.restaurant.mainScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurant.MainActivity
import com.example.restaurant.adapters.RestaurantAdapter
import com.example.restaurant.data.MyDatabase
import com.example.restaurant.data.Resource
import com.example.restaurant.data.repositories.RestaurantAppRepository
import com.example.restaurant.data.viewmodels.RestaurantViewModel
import com.example.restaurant.data.viewmodels.RestaurantViewModelFactory
import com.example.restaurant.databinding.FragmentMainScreenBinding
import kotlinx.android.synthetic.main.fragment_main_screen.*

class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    lateinit var viewModel: RestaurantViewModel
    lateinit var restaurantAdapter: RestaurantAdapter

    val TAG = "MainScreenFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel

        setUpRecyclerView()

        viewModel.restaurants.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let{restaurantsResponse ->
                        restaurantAdapter.setData(restaurantsResponse.restaurants)

                    }
                }
                is Resource.Error ->{
                    response.message?.let{ message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
            }

        })


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = MainScreenFragment()
    }

    private fun setUpRecyclerView(){
        restaurantAdapter = RestaurantAdapter()
        binding.mainRestaurantRecyclerView.adapter = restaurantAdapter
        binding.mainRestaurantRecyclerView.layoutManager = LinearLayoutManager(activity)
    }
}