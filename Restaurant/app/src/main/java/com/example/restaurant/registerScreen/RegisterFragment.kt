package com.example.restaurant.registerScreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.restaurant.MainActivity
import com.example.restaurant.R
import com.example.restaurant.data.User.User
import com.example.restaurant.data.viewmodels.UserViewModel
import com.example.restaurant.databinding.FragmentProfileScreenBinding
import com.example.restaurant.databinding.FragmentRegisterBinding
import com.example.restaurant.mainScreen.MainScreenFragmentDirections

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    lateinit var sp: SharedPreferences
    private lateinit var userViewModel: UserViewModel
    private lateinit var userList: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = this.requireContext().getSharedPreferences("userid", Context.MODE_PRIVATE);

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.registerButton.setOnClickListener{
            insertUserToDatabase()
        }

        (activity as MainActivity).bottomNavigation.setOnNavigationItemSelectedListener {
            if(it.title.toString() == "Home"){
                Log.v("NAVIGATION", it.title.toString())
                val action = RegisterFragmentDirections.actionRegisterFragmentToMainScreenFragment()
                findNavController().navigate(action)
            }

            false
        }

        userViewModel.readAllUsers.observe(viewLifecycleOwner, Observer {user ->
            userList = user
        })

        return binding.root
    }


    private fun insertUserToDatabase(){
        val firstName = binding.firstNameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val phoneNumber = binding.phoneNumberEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val address = binding.adressEditText.text.toString()

        if(inputCheck(firstName, lastName,password,phoneNumber,email,address)){
            if(checkIfUserExists(firstName, lastName)){
                Toast.makeText(this.requireContext(), "User already exists!", Toast.LENGTH_SHORT).show()
            }else {
                val user = User(0, firstName, lastName, password,phoneNumber,email,address)
                userViewModel.addUser(user)
                Toast.makeText(this.requireContext(), "Succesfully registered", Toast.LENGTH_SHORT).show()

                val action = RegisterFragmentDirections.actionRegisterFragmentToMainScreenFragment(user)
                findNavController().navigate(action)
            }
        }else{
            Toast.makeText(this.requireContext(), "Fill out all fields!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun inputCheck(firstName: String, lastName: String, password:String, phoneNumber:String, email:String, adress: String):Boolean{

        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) &&TextUtils.isEmpty(password)
                && TextUtils.isEmpty(phoneNumber) && TextUtils.isEmpty(email) && TextUtils.isEmpty(adress))
    }

    private fun checkIfUserExists(firstName: String, lastName: String): Boolean{
        for(element in userList){
            if(element.firstName == firstName && element.lastName == lastName){
                return true
            }
        }
        return false
    }
}