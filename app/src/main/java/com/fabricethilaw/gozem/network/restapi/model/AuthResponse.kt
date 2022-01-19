package com.fabricethilaw.gozem.network.restapi.model

import com.squareup.moshi.Json

data class AuthResponse(
    @Json(name = "message")
    val message: String
)