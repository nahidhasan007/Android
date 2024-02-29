package net.sharetrip.b2b.view.authentication.model

data class Credential(
    var username: String,
    var password: String
) {
    fun isValid(): Boolean =
        username.length >=3 && password.length >= 8
}
