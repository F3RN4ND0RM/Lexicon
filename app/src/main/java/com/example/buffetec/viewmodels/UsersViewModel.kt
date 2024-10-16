package com.example.buffetec.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.datastore.dataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buffetec.data.PreferencesKey.TOKEN
import com.example.buffetec.interfaces.UsersServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.buffetec.interfaces.LoginResponse
import com.example.buffetec.network.LoginRequest
import dataStore
import getRol
import getToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import saveRol
import saveToken


class UsersViewModel(private val usersService : UsersServices, application: Application) : AndroidViewModel(application) {


    private val token = MutableStateFlow<LoginState>(LoginState.Initial)
    val users: StateFlow<LoginState> =token
    val loginState: StateFlow<LoginState> = token



    fun login(username: String, password: String){
            viewModelScope.launch {

                try{

                    token.value = LoginState.Loading
                    val loginRequest = LoginRequest(username, password)
                    val response = UsersServices.instance.login(loginRequest)
                    token.value = LoginState.Success(response)
                    Log.d("respuesta", token.value.toString() )
                    getApplication<Application>().saveToken(response.token.toString())
                    getApplication<Application>().saveRol(response.rol.toString())


                }catch (e : Exception){
                    token.value = LoginState.Error("Login failed: ${e.message}")
                    Log.e("Error-APT",  "An error has ocurred: ${e.message.toString()}")
                }
            }

    }
}

sealed class LoginState{
    object Initial: LoginState()
    object Loading: LoginState()
    data class Success(val loginResponse: LoginResponse): LoginState()
    data class Error (val message : String): LoginState()
}