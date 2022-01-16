package com.fabricethilaw.gozem.network

import com.fabricethilaw.gozem.network.model.LoginPayload
import com.fabricethilaw.gozem.network.model.RegistrationPayload
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


private const val BASE_URL = "https://api.some.server/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val mockInterceptor = MockInterceptor()

private val client = OkHttpClient.Builder()
    .addInterceptor(mockInterceptor)
    .build()

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface ApiService {
    @POST("authenticate")
    suspend fun authenticate(@Body payload: LoginPayload): String

    @POST("register")
    suspend fun register(
        @Body payload: RegistrationPayload
    ): String

    @GET("profile")
    suspend fun getData(): Any
}

object Api {
    val webservice: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}