package com.example.restaurant.mainScreen

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.restaurant.MainActivity
import com.example.restaurant.R
import com.example.restaurant.adapters.RestaurantAdapter
import com.example.restaurant.data.Resource
import com.example.restaurant.data.User.User
import com.example.restaurant.data.restaurantEntityAndResponse.Restaurant
import com.example.restaurant.data.viewmodels.FavouriteViewModel
import com.example.restaurant.data.viewmodels.RestaurantImageViewModel
import com.example.restaurant.data.viewmodels.RestaurantViewModel
import com.example.restaurant.data.viewmodels.UserViewModel
import com.example.restaurant.databinding.FragmentMainScreenBinding

class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var viewModel: RestaurantViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantImageViewModel: RestaurantImageViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var sp: SharedPreferences
    private val args by navArgs<MainScreenFragmentArgs>()

    val TAG = "MainScreenFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = this.requireContext().getSharedPreferences("userid",MODE_PRIVATE);

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        restaurantImageViewModel= ViewModelProvider(this).get(RestaurantImageViewModel::class.java)
        favouriteViewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)

        (activity as MainActivity).bottomNavigation.setOnNavigationItemSelectedListener {
            if(it.title.toString() == "Profile"){
                val action = MainScreenFragmentDirections.actionMainScreenFragmentToProfileScreenFragment(args.user)
                findNavController().navigate(action)
            }
            false
        }


        setUpRecyclerView(args.user)



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

        restaurantImageViewModel.readAllRestaurantImages.observe(viewLifecycleOwner, Observer {
            restaurantAdapter.setImageData(it)
        })

        if(args.user != null) {
            favouriteViewModel.readFavouriteByUserId(args.user!!.id).observe(this.viewLifecycleOwner, {
                restaurantAdapter.setFavouriteData(it)
            })
        }

    }


    private fun setUpRecyclerView(user: User?){
        restaurantAdapter = RestaurantAdapter(user,true)
        binding.mainRestaurantRecyclerView.adapter = restaurantAdapter
        binding.mainRestaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }


}