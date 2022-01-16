package com.fabricethilaw.gozem.businesscase

import android.text.TextUtils
import android.util.Patterns


object Validator {

    fun validateName(
        text: String,
    ): Boolean {
        return text.isNotBlank()
    }

    fun validateEmail(
        text: CharSequence,
    ): Boolean {
        return !TextUtils.isEmpty(text.trim()) &&
                Patterns.EMAIL_ADDRESS.matcher(text).matches()

    }

    fun validatePassword(
        text: CharSequence
    ): Boolean {
        return text.isNotBlank() &&
                text.trim().length > 3 && text.trim().length < 100
    }


}