package com.example.crud.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    //contains the methods used for accessing the database
    @Insert
    suspend fun addUser(user: User)

    @Update
    suspend fun userUpdate(user: User)

    @Query("SELECT * FROM UserTable ORDER BY id ASC")
    fun readAllUserData(): LiveData<List<User>>

    @Delete
    suspend fun userDelete(user: User)

}