package com.example.restaurant.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.restaurant.data.MyDatabase
import com.example.restaurant.data.User.User
import com.example.restaurant.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    val readAllUsers: LiveData<List<User>>
    private val repository: UserRepository

    init{
        val userDao = MyDatabase.invoke(application.applicationContext).getUserDao()
        repository = UserRepository(userDao)
        readAllUsers = repository.readAllUsers
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO){
            repository.addUser(user)
        }
    }

    fun readUserById(userId: Int): LiveData<List<User>>{
        return repository.readUserById(userId)
    }

}