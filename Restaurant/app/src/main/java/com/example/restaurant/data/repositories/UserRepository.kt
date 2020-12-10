package com.example.restaurant.data.repositories

import androidx.lifecycle.LiveData
import com.example.restaurant.data.User.User
import com.example.restaurant.data.daos.UserDao

class UserRepository(private val userDao: UserDao) {

    /**
     * Gets all users from the user table
     */
    val readAllUsers: LiveData<List<User>> = userDao.readAllUsers()

    /**
     * Add new user into the user table
     * @param user - this element will be added into the user table
     */
    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    /**
     * Deletes user from user table
     * @param user - this user will be deleted from the user table
     */
    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    /**
     * Gets user from user table based on user ID
     * @param userId - search parameter for user ID
     */
    fun readUserById(userId: Int):LiveData<List<User>>{
        return userDao.readUserById(userId)
    }

}