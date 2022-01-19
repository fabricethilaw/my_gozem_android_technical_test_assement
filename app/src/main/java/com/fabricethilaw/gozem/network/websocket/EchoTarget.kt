package com.fabricethilaw.gozem.network.websocket

interface EchoTarget {
    fun setMessages(messages: List<String>)

    fun addMessage(message: String)

    fun clearAllMessages()

}
