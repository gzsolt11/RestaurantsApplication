package com.example.restaurant.loginScreen

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurant.MainActivity
import com.example.restaurant.data.User.User
import com.example.restaurant.data.viewmodels.UserViewModel
import com.example.restaurant.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    lateinit var sp: SharedPreferences
    private lateinit var userViewModel: UserViewModel
    private lateinit var userList: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sp = this.requireContext().getSharedPreferences("userid", Context.MODE_PRIVATE);

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.registerButton.setOnClickListener{
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        binding.loginButton.setOnClickListener{
            loginUser()
        }

        (activity as MainActivity).bottomNavigation.setOnNavigationItemSelectedListener {
            if(it.title.toString() == "Home"){
                Log.v("NAVIGATION", it.title.toString())
                val action = LoginFragmentDirections.actionLoginFragmentToMainScreenFragment()
                findNavController().navigate(action)
            }

            false
        }

        userViewModel.readAllUsers.observe(viewLifecycleOwner, Observer {user ->
            userList = user
        })

        return binding.root
    }

    private fun loginUser(){
        val firstName = binding.firstNameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if(inputCheck(firstName, lastName,password)){
            val user = getUser(firstName, lastName,password)
            if(user == null){
                Toast.makeText(this.requireContext(), "Invalid inputs!", Toast.LENGTH_SHORT).show()
            }else {
                val action = LoginFragmentDirections.actionLoginFragmentToMainScreenFragment(user)
                findNavController().navigate(action)
            }
        }else{
            Toast.makeText(this.requireContext(), "Fill out all fields!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun inputCheck(firstName: String, lastName: String, password:String):Boolean{

        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && TextUtils.isEmpty(password) )
    }

    private fun getUser(firstName: String, lastName: String,password:String): User?{
        for(element in userList){
            if(element.firstName == firstName && element.lastName == lastName && element.password == password){
                return element
            }
        }
        return null
    }

}