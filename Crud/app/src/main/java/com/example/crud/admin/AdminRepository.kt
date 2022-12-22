package com.example.crud.admin

import androidx.lifecycle.LiveData

class AdminRepository(private val adminDao:AdminUserDao) {
    val adminData:LiveData<List<Superadmin>> = adminDao.readAllAdminData()
    suspend fun addAdmin(admin: Superadmin)
    {
        adminDao.addAdmin(admin)
    }
    suspend fun adminLogin(userName:String,passWord:String)=adminDao.checkAdmin(userName,passWord)
}