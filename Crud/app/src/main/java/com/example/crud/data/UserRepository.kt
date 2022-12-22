package com.example.crud.data

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {
    val realAllData:LiveData<List<User>> = userDao.readAllUserData()
    suspend fun addUser(user: User)
    {
        userDao.addUser(user)
    }
    suspend fun userUpdate(user: User)
    {
        userDao.userUpdate(user)
    }
    suspend fun userDelete(user:User)
    {
        userDao.userDelete(user)
    }

}