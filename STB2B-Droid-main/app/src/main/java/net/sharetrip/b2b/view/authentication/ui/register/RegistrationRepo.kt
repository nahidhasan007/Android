package net.sharetrip.b2b.view.authentication.ui.register

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.AuthEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.authentication.model.AgentInformation
import net.sharetrip.b2b.view.authentication.model.UserProfile

class RegistrationRepo(private val endPoint: AuthEndPoint) {

    suspend fun signUpAgent(credential: AgentInformation): GenericResponse<RestResponse<UserProfile>> =
        endPoint.signUpAgent(credential)
}