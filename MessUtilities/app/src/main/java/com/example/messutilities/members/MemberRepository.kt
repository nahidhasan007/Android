package com.example.messutilities.members

import android.util.Log
import javax.inject.Inject

class MemberRepository @Inject constructor(){

    fun saveMember(email : String, id: String){
        Log.d(MEMBER_INFO,"Member save in Db with email : {$email} and id: {$id}")
    }
    companion object{
        const val MEMBER_INFO = "MEMBER_INFO"
    }
}
