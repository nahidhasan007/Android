package net.sharetrip.b2b.view.authentication.ui.forget

import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.AuthEndPoint
import net.sharetrip.b2b.network.GenericResponse

class ForgetPasswordRepo(private val endPoint: AuthEndPoint) {
    suspend fun sendResetPasswordEmail(email: String): GenericResponse<RestResponse<EmptyResponse>> =
        endPoint.getResetPasswordLinkThroughEmail(email)
}
