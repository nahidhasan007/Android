package com.example.mvvmapp.quote

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// Dao is used to manage database and interact with the data
@Dao
interface QuoteDao {

    @Query("SELECT * FROM quote")
    fun getQuote() : LiveData<List<Quote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: Quote)
}