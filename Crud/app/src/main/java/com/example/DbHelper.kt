package com.example

import android.app.Application
import com.example.crud.data.UserDatabase

object DbHelper {
    var database: UserDatabase? = null

    fun initDatabase(application: Application) {
        database = UserDatabase.getDatabase(application)
    }

    fun getDatabase(): UserDatabase? {
        return database
    }
}