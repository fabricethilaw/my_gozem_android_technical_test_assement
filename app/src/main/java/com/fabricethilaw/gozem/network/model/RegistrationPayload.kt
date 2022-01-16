package com.fabricethilaw.gozem.network.model

data class RegistrationPayload(
    val fullName: String,
    val email: String,
    val password: String
)