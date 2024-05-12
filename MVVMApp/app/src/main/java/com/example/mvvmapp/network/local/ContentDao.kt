package com.example.mvvmapp.network.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmapp.model.Content

// Dao is used to manage database and interact with the data
@Dao
interface ContentDao {

    @Query("SELECT * FROM quote")
    fun getQuote() : LiveData<List<Content>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: Content)
}