package com.example.restaurant.profileScreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.restaurant.MainActivity
import com.example.restaurant.R
import com.example.restaurant.data.User.User
import com.example.restaurant.data.viewmodels.RestaurantViewModel
import com.example.restaurant.data.viewmodels.UserViewModel
import com.example.restaurant.databinding.FragmentProfileScreenBinding
import com.example.restaurant.mainScreen.MainScreenFragmentArgs
import com.example.restaurant.mainScreen.MainScreenFragmentDirections


class ProfileScreenFragment : Fragment() {

    private lateinit var binding: FragmentProfileScreenBinding
    lateinit var viewModel: RestaurantViewModel
    lateinit var sp: SharedPreferences
    lateinit var userViewModel: UserViewModel
    private val args by navArgs<MainScreenFragmentArgs>()
    private lateinit var userList: List<User>
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = this.requireContext().getSharedPreferences("userid", Context.MODE_PRIVATE)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileScreenBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        if(args.user != null){
            currentUser = args.user
            binding.userNameTextView.text = getString(R.string.whole_name,currentUser?.lastName, currentUser?.firstName)
            binding.adressTextView.text = currentUser?.address
            binding.phoneNumberTextView.text =currentUser?.phoneNumber
            binding.emailTextView.text = currentUser?.email
        } else{
            val action = ProfileScreenFragmentDirections.actionProfileScreenFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        (activity as MainActivity).bottomNavigation.setOnNavigationItemSelectedListener {
            if(it.title.toString() == "Home"){
                val action = ProfileScreenFragmentDirections.actionProfileScreenFragmentToMainScreenFragment(args.user)
                findNavController().navigate(action)
            }

            false
        }

        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.logoutButton.setOnClickListener{
            Navigation.findNavController(this.requireView()).navigate(R.id.action_profileScreenFragment_to_mainScreenFragment)

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ProfileScreenFragment()
    }
}