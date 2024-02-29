package net.sharetrip.b2b.model

data class RestResponse<T> (
    val code: String,
    val message: String,
    val response: T
)
