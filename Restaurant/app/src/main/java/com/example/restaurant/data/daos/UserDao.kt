package com.example.restaurant.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.restaurant.data.User.User

@Dao
interface UserDao {

    /**
     * Add new element into the user_table
     * @param user - this element will be added to the table
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    /**
     * Gets all users from the user_table
     */
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllUsers(): LiveData<List<User>>

    /**
     * Gets all users based on a user ID
     * @param userId - search parameter for user ID
     */
    @Query("SELECT * FROM user_table WHERE id=:userId")
    fun readUserById(userId: Int): LiveData<List<User>>

    /**
     * Deletes a user from the user table
     * @param user - this element will be deleted from the table
     */
    @Delete()
    suspend fun deleteUser(user: User)
}