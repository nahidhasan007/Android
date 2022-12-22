package com.example.crud.admin

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.crud.data.User

@Dao
interface AdminUserDao {
    @Insert
    suspend fun addAdmin(admin:Superadmin)

    @Query("SELECT * FROM Admin ORDER BY id ASC")
    fun readAllAdminData(): LiveData<List<Superadmin>>



    @Query("SELECT * FROM Admin WHERE username LIKE :name and password LIKE:pass")
    fun checkAdmin(name:String,pass:String) : List<Superadmin>
}