package com.fabricethilaw.gozem.network.restapi.model

import com.squareup.moshi.Json

data class LoginPayload(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String
)