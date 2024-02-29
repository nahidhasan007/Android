package net.sharetrip.b2b.network

import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.view.authentication.model.AgentInformation
import net.sharetrip.b2b.view.authentication.model.Credential
import net.sharetrip.b2b.view.authentication.model.UserProfile
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthEndPoint {

    @POST("auth/login")
    suspend fun fetchUserByEmailLogin(@Body credential: Credential): GenericResponse<RestResponse<UserProfile>>

    @POST("user/forgot-password")
    @FormUrlEncoded
    suspend fun getResetPasswordLinkThroughEmail(@Field("email") email: String): GenericResponse<RestResponse<EmptyResponse>>

    @POST("auth/signup")
    suspend fun signUpAgent(@Body credential: AgentInformation): GenericResponse<RestResponse<UserProfile>>
}
