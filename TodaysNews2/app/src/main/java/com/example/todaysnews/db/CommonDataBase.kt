package com.example.todaysnews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todaysnews.model.Post

@Database(entities = [Post::class], version = 1)
abstract class CommonDataBase : RoomDatabase() {
    abstract fun DbDao() : DbDao
    companion object {
        private var Instance : CommonDataBase? = null
        fun getDatabase(context: Context):CommonDataBase{
            val tempInstance = Instance
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CommonDataBase::class.java,
                    "PostsDb"
                ).build()
                Instance = instance
                return instance
            }
        }
    }
}