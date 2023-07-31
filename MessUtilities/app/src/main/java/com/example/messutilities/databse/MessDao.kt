package com.example.messutilities.databse

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.messutilities.model.Members

@Dao
interface MessDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(members: Members) : Long

}