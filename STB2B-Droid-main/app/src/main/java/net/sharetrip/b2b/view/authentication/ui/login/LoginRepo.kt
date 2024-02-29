package net.sharetrip.b2b.view.authentication.ui.login

import net.sharetrip.b2b.localdb.UserProfileDao
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.model.STException
import net.sharetrip.b2b.network.AuthEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.authentication.model.Credential
import net.sharetrip.b2b.view.authentication.model.UserProfile

class LoginRepo(private val endPoint: AuthEndPoint, private val userDao: UserProfileDao) {

    suspend fun fetchUserByEmailLogin(credential: Credential): GenericResponse<RestResponse<UserProfile>> =
        endPoint.fetchUserByEmailLogin(credential)

    suspend fun saveUserProfile(user: UserProfile) {
        try {
            userDao.saveUserProfile(user)
        } catch (throwable: Throwable) {
            throw STException("Fail to save user profile", throwable);
        }
    }
}
