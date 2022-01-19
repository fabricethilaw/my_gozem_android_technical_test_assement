package com.fabricethilaw.gozem.network.restapi.model

data class RegistrationPayload(
    val fullName: String,
    val email: String,
    val password: String
)