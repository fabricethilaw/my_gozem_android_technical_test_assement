package com.fabricethilaw.gozem.network.websocket

import android.annotation.SuppressLint
import android.util.Log
import com.tinder.scarlet.Event
import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.State
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicReference

@SuppressLint("CheckResult")
class MessageRepository constructor(
    private val echoService: EchoService
) {
    private val messagesRef = AtomicReference<List<String>>()
    private val messagesProcessor = BehaviorProcessor.create<List<String>>()

    init {
        echoService.observeEvent()
            .observeOn(Schedulers.io())
            .subscribe({ event ->
                val description = when (event) {
                    is Event.OnLifecycle.StateChange<*> -> when (event.state) {
                        Lifecycle.State.Started -> "[WS] \uD83C\uDF1D On Lifecycle Start - ${getTimeStamp()}"
                        is Lifecycle.State.Stopped -> "[WS] \uD83C\uDF1A On Lifecycle Stop - ${getTimeStamp()}"
                        Lifecycle.State.Destroyed -> "[WS] \uD83D\uDCA5 On Lifecycle Terminate - ${getTimeStamp()}"
                    }
                    Event.OnLifecycle.Terminate -> "[WS] \uD83D\uDCA5 On Lifecycle Terminate - ${getTimeStamp()}"

                    is Event.OnWebSocket.Event<*> -> when (event.event) {
                        is WebSocket.Event.OnConnectionOpened<*> -> "\uD83D\uDEF0️ On WebSocket Connection Opened - ${getTimeStamp()}"
                        is WebSocket.Event.OnMessageReceived -> "\uD83D\uDEF0️ On WebSocket Message Received - ${getTimeStamp()}"
                        is WebSocket.Event.OnConnectionClosing -> "\uD83D\uDEF0️ On WebSocket Connection Closing - ${getTimeStamp()}"
                        is WebSocket.Event.OnConnectionClosed -> "\uD83D\uDEF0️ On WebSocket Connection Closed - ${getTimeStamp()}"
                        is WebSocket.Event.OnConnectionFailed -> "\uD83D\uDEF0️ On WebSocket Connection Failed - ${getTimeStamp()}"
                    }

                    Event.OnWebSocket.Terminate -> "\uD83D\uDEF0️ On WebSocket Terminate"
                    is Event.OnStateChange<*> -> when (event.state) {
                        is State.WaitingToRetry -> "WebSocket \uD83D\uDCA4 WaitingToRetry - ${getTimeStamp()}"
                        is State.Connecting -> "WebSocket Connecting - ${getTimeStamp()}"
                        is State.Connected -> "WebSocket \uD83D\uDEEB Connected - ${getTimeStamp()}"
                        State.Disconnecting -> "WebSocket Disconnecting - ${getTimeStamp()}"
                        State.Disconnected -> "WebSocket\uD83D\uDEEC Disconnected - ${getTimeStamp()}"
                        State.Destroyed -> "WebSocket \uD83D\uDCA5 Destroyed - ${getTimeStamp()}"
                    }
                    Event.OnRetry -> "[WS] ⏰ On Retry - ${getTimeStamp()}"
                }
                addMessage(description)
            }, { e ->
                Log.e("Websocket error", e.message.toString())
            })

        echoService.observeText()
            .observeOn(Schedulers.io())
            .subscribe({ text ->
                addMessage(text)
            }, { e ->
                Log.e("Websocket error", e.message.toString())
            })

    }

    fun observeMessage(): Flowable<List<String>> = messagesProcessor


    private fun addMessage(message: String) {
        val existingMessages = messagesRef.get() ?: listOf()
        val messages = existingMessages + message
        messagesRef.set(messages)
        messagesProcessor.onNext(messages)
    }

    private fun getTimeStamp(): String {
        return SimpleDateFormat("hh:mm a").format(Date())
    }

    fun addMessageToSend(text: String) {
        addMessage(text)
        echoService.sendText(text)
    }
}
