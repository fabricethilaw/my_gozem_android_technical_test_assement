package com.fabricethilaw.gozem.network.websocket

import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MessagePresenter constructor(
    private val messageRepository: MessageRepository
) {

    private var targetState: TargetState = TargetState.MightBeBehind(0)
    private lateinit var target: EchoTarget

    private val compositeDisposable = CompositeDisposable()

    fun takeTarget(target: EchoTarget) {
        this.target = target
        setup()
    }

    fun dropTarget() {
        compositeDisposable.clear()
    }

    private fun setup() {
        val messageSubscription = messageRepository.observeMessage()
            .observeOn(Schedulers.io())
            .subscribe({ messages ->
                when (val targetState = targetState) {
                    is TargetState.MightBeBehind -> {
                        if (targetState.messageCount == 0) {
                            target.setMessages(messages)
                        } else {
                            for (i in targetState.messageCount until messages.size) {
                                target.addMessage(messages[i])
                            }
                        }
                        this.targetState = TargetState.UpToDate
                    }
                    TargetState.UpToDate -> {
                        if (messages.isEmpty()) {
                            target.clearAllMessages()
                        } else {
                            target.addMessage(messages.last())
                        }
                    }
                }
            }, { e ->
                Log.e("WebSocket error", e.message.toString())
            })

        compositeDisposable.addAll(messageSubscription)
    }

    sealed class TargetState {
        data class MightBeBehind(val messageCount: Int) : TargetState()
        object UpToDate : TargetState()
    }

}
