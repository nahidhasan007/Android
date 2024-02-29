package net.sharetrip.b2b.view.more.moreinfo

import net.sharetrip.b2b.localdb.UserProfileDao
import net.sharetrip.b2b.model.STException
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.view.authentication.model.UserProfile

class MoreRepo(private var userProfileDao: UserProfileDao?) {
    suspend fun deleteUser() {
        try {
            userProfileDao!!.deleteUser()
        } catch (throwable: Throwable) {
            //throw STException("Fail to delete user", throwable);
        }

        AppSharedPreference.accessToken = ""
    }

    suspend fun getUserProfile(): UserProfile {
        return userProfileDao?.getUserprofile()!!
    }
}