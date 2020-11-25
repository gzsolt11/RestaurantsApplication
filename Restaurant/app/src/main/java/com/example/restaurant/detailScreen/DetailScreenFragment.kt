package com.example.restaurant.detailScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.restaurant.MainActivity
import com.example.restaurant.data.viewmodels.RestaurantViewModel
import com.example.restaurant.databinding.FragmentDetailScreenBinding
import com.example.restaurant.mainScreen.MainScreenFragmentDirections

class DetailScreenFragment : Fragment() {

    private lateinit var binding: FragmentDetailScreenBinding
    lateinit var viewModel: RestaurantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailScreenBinding.inflate(inflater, container, false)

        binding.dropDownImageView.setOnClickListener{
            if(binding.detailsConstraintLayout.visibility == View.GONE){
                binding.detailsConstraintLayout.visibility = View.VISIBLE
            } else{
                binding.detailsConstraintLayout.visibility = View.GONE
            }
        }

        (activity as MainActivity).bottomNavigation.setOnNavigationItemSelectedListener {
            if(it.title.toString() == "Profile"){
                val action = DetailScreenFragmentDirections.actionDetailScreenFragmentToProfileScreenFragment()
                findNavController().navigate(action)
            }
            if(it.title.toString() == "Home"){
                val action = DetailScreenFragmentDirections.actionDetailScreenFragmentToMainScreenFragment()
                findNavController().navigate(action)
            }

            false
        }

        viewModel = (activity as MainActivity).viewModel

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = DetailScreenFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}