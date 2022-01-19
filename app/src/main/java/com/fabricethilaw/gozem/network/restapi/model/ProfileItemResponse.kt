package com.fabricethilaw.gozem.network.restapi.model

import com.squareup.moshi.Json

data class ProfileItemResponse(
    @Json(name = "type")
    val type: String,
    @Json(name = "content")
    val content: Map<String, Any>
)


fun Map<String, Any>.mapToProfileContent(): ProfileContent {
    val image: String = this["image"].toString()
    val name: String = this["name"].toString()
    val email: String = this["email"].toString()
    return ProfileContent(image, name, email)
}

fun Map<String, Any>.mapToLocationContent(): LocationContent {
    val title: String = this["title"].toString()
    val mapMarker: String = this["pin"].toString()
    val latitude: Double? = this["lat"] as? Double
    val longitude: Double? = this["lng"] as? Double

    return LocationContent(title, mapMarker, latitude, longitude)
}

fun Map<String, Any>.mapToInformationContent(): InformationContent {
    val title: String = this["title"].toString()
    val source: String = this["source"].toString()
    val value: String = this["value"].toString()

    return InformationContent(title, source, value)
}

data class ProfileContent(val image: String?, val name: String?, val email: String?)
data class LocationContent(
    val title: String?,
    val mapMarker: String?,
    val latitude: Double?,
    val longitude: Double?
)

data class InformationContent(val title: String?, val source: String?, val value: String?)