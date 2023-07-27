package com.example.messutilities.shared

import android.app.Application
import com.example.messutilities.members.MemberRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

//    @Inject
//    private lateinit var memberRepository: MemberRepository

    override fun onCreate() {
        super.onCreate()
//        memberRepository.saveMember("nahidhasan007@gmail.com","17008")
    }
}