package com.example.mvvmapp.network.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvmapp.model.Content

@Database(entities = [Content::class], version = 1,exportSchema=false)
abstract class ContentDatabase : RoomDatabase() {
    abstract fun quoteDao(): ContentDao
    companion object{
        private var Instance : ContentDatabase?=null
        fun getDatabase(context: Context): ContentDatabase {
            val tempInstanc = Instance
            if(tempInstanc!=null)
            {
                return tempInstanc
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContentDatabase::class.java,
                    "quote"
                ).build()
                Instance = instance
                return instance
            }
        }
    }

}