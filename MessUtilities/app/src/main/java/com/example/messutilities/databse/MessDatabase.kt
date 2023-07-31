package com.example.messutilities.databse

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.messutilities.model.Members

@Database(entities = [Members::class], version = 1)
abstract class MessDatabase : RoomDatabase() {

    abstract fun messDao() : MessDao

    companion object {
        @Volatile private var databaseInstance : MessDatabase? = null

        fun getInstance(context: Context) : MessDatabase {
            return databaseInstance ?: synchronized(this) {
                databaseInstance ?: buildDataBase(context).also { databaseInstance = it }
            }
        }
        private fun buildDataBase(context: Context) : MessDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MessDatabase::class.java,
                name = "Mess_Database"
            ).build()
        }
    }
}