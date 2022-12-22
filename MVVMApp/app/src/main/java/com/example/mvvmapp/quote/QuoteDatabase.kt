package com.example.mvvmapp.quote

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Quote::class], version = 1,exportSchema=false)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
    companion object{
        private var Instance : QuoteDatabase?=null
        fun getDatabase(context: Context):QuoteDatabase{
            val tempInstanc = Instance
            if(tempInstanc!=null)
            {
                return tempInstanc
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java,
                    "quote"
                ).build()
                Instance = instance
                return instance
            }
        }
    }

}