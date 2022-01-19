package com.fabricethilaw.gozem.network.websocket

import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import okhttp3.OkHttpClient

class WebSocketServiceProvider {

    fun getWebSocketClient(url: String): EchoService {
        val client = provideOkHttpClient()
        return provideService(url, client)
    }

    private fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()


    private fun provideService(
        url: String,
        client: OkHttpClient
    ): EchoService {

        val scarlet = Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(url))
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()
        return scarlet.create()
    }
}