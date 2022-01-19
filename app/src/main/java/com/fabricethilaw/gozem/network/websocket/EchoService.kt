package com.fabricethilaw.gozem.network.websocket

import com.tinder.scarlet.Event
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface EchoService {
    @Receive
    fun observeEvent(): Flowable<Event>

    @Receive
    fun observeText(): Flowable<String>

    @Send
    fun sendText(message: String): Boolean


}