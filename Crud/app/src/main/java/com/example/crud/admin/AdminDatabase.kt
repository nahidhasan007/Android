package com.example.crud.admin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.crud.data.UserDatabase

@Database(entities = [Superadmin::class], version = 1, exportSchema = false)
abstract class AdminDatabase : RoomDatabase() {
    abstract fun AdminUserDao(): AdminUserDao

    companion object {
        private var Instance: AdminDatabase? = null
        fun getDatabase(context: Context): AdminDatabase{
            val tempInstance = Instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AdminDatabase::class.java, "admin_database"
                ).build()
                Instance = instance
                return instance
            }
        }

    }
}