package com.fabricethilaw.gozem.network

import com.fabricethilaw.gozem.network.model.AuthResponse
import com.fabricethilaw.gozem.network.model.LoginPayload
import com.fabricethilaw.gozem.network.model.RegistrationPayload
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


private const val BASE_URL =
    "https://a290d873-24d9-450e-9628-8ee4a919921c.mock.pstmn.io/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(OkHttpClient.Builder().build())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface ApiService {

    @POST("authenticate")
    fun authenticate(@Body payload: LoginPayload): Call<AuthResponse>

    @POST("register")
    suspend fun register(
        @Body payload: RegistrationPayload
    ): Response<AuthResponse>

    @GET("profile")
    suspend fun getData(): Response<Any>

}

object Api {
    val webservice: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}