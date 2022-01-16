package com.fabricethilaw.gozem.businesscase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabricethilaw.gozem.network.Api
import com.fabricethilaw.gozem.network.model.LoginPayload
import com.fabricethilaw.gozem.network.model.RegistrationPayload
import kotlinx.coroutines.launch

class BusinessCaseViewModel : ViewModel() {

    fun signIn(
        email: String,
        password: String,
        onError: (String) -> Unit,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = Api.webservice.authenticate(LoginPayload(email, password))
                onSuccess(response)
            } catch (e: Exception) {
                onError(e.message.toString())
            }
        }

    }

    fun register(
        name: String,
        email: String,
        password: String,
        onError: (String) -> Unit,
        onSuccess: (String) -> Unit
    ) {

        viewModelScope.launch {
            try {
                val response = Api.webservice.register(RegistrationPayload(name, email, password))
                onSuccess.invoke(response)
            } catch (e: Exception) {
                onError.invoke(e.message.toString())
            }
        }

    }



}