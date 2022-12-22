package com.example.crud.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class],version=1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun UserDao():UserDao
    companion object{
        private var Instance:UserDatabase?=null
        fun getDatabase(context: Context):UserDatabase{
            val tempInstance = Instance
            if(tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,"user_database"
                ).build()
                Instance = instance
                return instance
            }
        }
    }
}
