package com.example.restaurant.mainScreen

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var viewModel: RestaurantViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var restaurantImageViewModel: RestaurantImageViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var sp: SharedPreferences
    private val args by navArgs<MainScreenFragmentArgs>()
    private val QUERY_PAGE_SIZE = 25

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


        setUpRecyclerView(args.user,true)

       /* binding.queryParameterSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(parent?.getItemAtPosition(position).toString() == "Favourites"){

                } else{
                    restaurantAdapter.changeBackFromFavourites()
                }
            }

        }*/

        var adapter = ArrayAdapter.createFromResource(this.requireContext(),R.array.query_parameters, R.layout.spinner_item)
        binding.queryParameterSpinner.adapter = adapter

        binding.favouriteImageView.setOnClickListener{
            if(!restaurantAdapter.isFavourites) {
                if (args.user == null) {
                    Toast.makeText(this@MainScreenFragment.requireContext(), "Log in for favourites!", Toast.LENGTH_SHORT).show()
                }else{
                    restaurantAdapter.changeToFavourites()
                    binding.favouriteImageView.setImageResource(R.drawable.ic_yellow_star)
                }
            }else{
                restaurantAdapter.changeBackFromFavourites()
                binding.favouriteImageView.setImageResource(R.drawable.ic_white_star)
            }

        }

        var job: Job? = null
        binding.searchEditText.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let{
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchRestaurantResponse = null
                        viewModel.searchRestaurantsPage = 1
                        when(binding.queryParameterSpinner.selectedItem.toString()){
                            "Country" -> viewModel.searchRestaurantsByCountry(binding.searchEditText.text.toString())
                            "State" -> viewModel.searchRestaurantsByState(binding.searchEditText.text.toString())
                            "Price" -> viewModel.searchRestaurantsByPrice("us",binding.searchEditText.text.toString())
                            "Name" -> viewModel.searchRestaurantsByName(binding.searchEditText.text.toString())
                            "City" -> viewModel.searchRestaurantsByCity(binding.searchEditText.text.toString())
                            "Address" -> viewModel.searchRestaurantsByAddress(binding.searchEditText.text.toString())
                        }
                    }/*else{
                        viewModel.searchRestaurantResponse = null
                        viewModel.searchRestaurantsPage = 1
                        viewModel.searchRestaurantsByCountry("us")
                        binding.queryParameterSpinner.setSelection(1)
                    }*/
                }
            }
        }

        viewModel.searchRestaurants.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let{restaurantsResponse ->
                        restaurantAdapter.setData(restaurantsResponse.restaurants)
                        val totalPages = restaurantsResponse.total_entries / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.restaurantsPage == totalPages
                    }
                }
                is Resource.Error ->{
                    response.message?.let{ message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
            }
        })

        viewModel.searchRestaurantsByCountry("us")

        restaurantImageViewModel.readAllRestaurantImages.observe(viewLifecycleOwner, Observer {
            restaurantAdapter.setImageData(it)
        })

        if(args.user != null) {
            favouriteViewModel.readFavouriteByUserId(args.user!!.id).observe(this.viewLifecycleOwner, {
                restaurantAdapter.setFavouriteData(it)
            })
        }

    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object :RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible
                    && isScrolling
            if(shouldPaginate){
                viewModel.getRestaurants("US")
                isScrolling = false
            }
        }
    }

    private fun setUpRecyclerView(user: User?, isMainscreenAdapter:Boolean){
        restaurantAdapter = RestaurantAdapter(user,isMainscreenAdapter)
        binding.mainRestaurantRecyclerView.adapter = restaurantAdapter
        binding.mainRestaurantRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRestaurantRecyclerView.addOnScrollListener(this@MainScreenFragment.scrollListener)

    }


}