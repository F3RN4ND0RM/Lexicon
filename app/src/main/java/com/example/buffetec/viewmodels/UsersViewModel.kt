package com.example.buffetec.viewmodels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.datastore.core.IOException
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
import com.example.buffetec.interfaces.RegistrarResponse
import com.example.buffetec.interfaces.UpdateRequest
import com.example.buffetec.interfaces.User
import com.example.buffetec.interfaces.UsersResponse
import com.example.buffetec.interfaces.updateResponse
import com.example.buffetec.interfaces.updateRolrequest
import com.example.buffetec.interfaces.usersALL
import com.example.buffetec.network.LoginRequest
import com.example.buffetec.network.RegisterRequest
import dataStore
import getRol
import getToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import saveRol
import saveToken


class UsersViewModel(private val usersService : UsersServices, application: Application) : AndroidViewModel(application) {


    private val token = MutableStateFlow<LoginState>(LoginState.Initial)
    val allusersState = MutableStateFlow<AllUsersState>(AllUsersState.Initial)
    val signupstate = MutableStateFlow<SignUpState>(SignUpState.Initial)
    val updatestate = MutableStateFlow<UpdateState>(UpdateState.Initial)
    val users: StateFlow<LoginState> =token
    val loginState: StateFlow<LoginState> = token
    val usersbyidState =  MutableStateFlow<GetUserByIdState>(GetUserByIdState.Initial)
    val userrolstate = MutableStateFlow<RolUserState>(RolUserState.Initial)
    val rolstate = MutableStateFlow<rol>(rol.Initial)

    fun getRol(){
        viewModelScope.launch {
            val _rol = getApplication<Application>().getRol()

            try {
                rolstate.value = rol.Loading
                rolstate.value = rol.Success(_rol)


            }catch (e : Exception){
                Log.e("Error-APT",  "An error has ocurred: ${e.message.toString()}")
            }
        }
    }

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

    fun registrar(name: String,
                  surname: String,
                  gender: String,
                  email: String,
                  address: String,
                  neighborhood:String,
                  password: String,
                  city : String,
                  state : String,
                  cp: String,
                  phone: String,
                  rol: String,
                  AUP: Boolean){

            viewModelScope.launch{
                val signupRequest = RegisterRequest( name,surname,gender,email,address,neighborhood,password,city,state,cp,phone,rol,AUP)
                Log.d("respuesta", signupRequest.toString())
                try{
                    signupstate.value = SignUpState.Loading

                    val response = UsersServices.instance.registrar(signupRequest)
                    signupstate.value = SignUpState.Success(response)
                    Log.d("respuesta", signupstate.value.toString() )

                }catch (e: Exception) {
                    when (e) {
                        is HttpException -> {
                            val errorBody = e.response()?.errorBody()?.string() ?: "No error body"
                            val errorCode = e.code()

                            // Actualizar el estado con información más específica
                            signupstate.value = SignUpState.Error("Login failed: HTTP $errorCode - ${e.message()}")

                            // Log más detallado
                            Log.e("Error-APT", "HTTP error: $errorCode - ${e.message()}")
                            Log.e("Error-APT", "Error body: $errorBody")
                        }
                        is IOException -> {
                            // Posible error de red (sin conexión, tiempo de espera agotado, etc.)
                            signupstate.value = SignUpState.Error("Login failed: Network error - ${e.message}")
                            Log.e("Error-APT", "Network error: ${e.message}")
                        }
                        else -> {
                            // Cualquier otro tipo de error
                            signupstate.value = SignUpState.Error("Login failed: ${e.message}")
                            Log.e("Error-APT", "An error has occurred: ${e.message.toString()}")
                        }
                    }
                }

            }

    }

    fun getusuariobyid(){
        viewModelScope.launch {
            val _token = getApplication<Application>().getToken()

            Log.d("respuesta", _token.toString())
            try{
                signupstate.value = SignUpState.Loading

                val response = UsersServices.instance.userbyid(_token)
                usersbyidState.value = GetUserByIdState.Success(response)
                Log.d("respuesta", usersbyidState.value.toString() )

            }catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string() ?: "No error body"
                        val errorCode = e.code()

                        // Actualizar el estado con información más específica
                        usersbyidState.value = GetUserByIdState.Error("Login failed: HTTP $errorCode - ${e.message()}")

                        // Log más detallado
                        Log.e("Error-APT", "HTTP error: $errorCode - ${e.message()}")
                        Log.e("Error-APT", "Error body: $errorBody")
                    }
                    is IOException -> {
                        // Posible error de red (sin conexión, tiempo de espera agotado, etc.)
                        usersbyidState.value = GetUserByIdState.Error("Login failed: Network error - ${e.message}")
                        Log.e("Error-APT", "Network error: ${e.message}")
                    }
                    else -> {
                        // Cualquier otro tipo de error
                        usersbyidState.value = GetUserByIdState.Error("Login failed: ${e.message}")
                        Log.e("Error-APT", "An error has occurred: ${e.message.toString()}")
                    }
                }
            }
            }
    }


    fun actualizar(name: String,
                  surname: String,
                  email: String,
                  address: String,
                  city : String,){

        viewModelScope.launch{
            val _token = getApplication<Application>().getToken()
            val updateRequest = UpdateRequest( name,surname,email,address,city)
            Log.d("respuesta", updateRequest.toString())
            try{
                updatestate.value = UpdateState.Loading

                val response = UsersServices.instance.updateuser(_token, updateRequest)
                updatestate.value = UpdateState.Success(response)
                Log.d("respuesta", updatestate.value.toString() )

            }catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string() ?: "No error body"
                        val errorCode = e.code()

                        // Actualizar el estado con información más específica
                        updatestate.value = UpdateState.Error("Login failed: HTTP $errorCode - ${e.message()}")

                        // Log más detallado
                        Log.e("Error-APT", "HTTP error: $errorCode - ${e.message()}")
                        Log.e("Error-APT", "Error body: $errorBody")
                    }
                    is IOException -> {
                        // Posible error de red (sin conexión, tiempo de espera agotado, etc.)
                        updatestate.value = UpdateState.Error("Login failed: Network error - ${e.message}")
                        Log.e("Error-APT", "Network error: ${e.message}")
                    }
                    else -> {
                        // Cualquier otro tipo de error
                        updatestate.value = UpdateState.Error("Login failed: ${e.message}")
                        Log.e("Error-APT", "An error has occurred: ${e.message.toString()}")
                    }
                }
            }

        }

    }

    fun updateRol(_id: String, _rol : String){


        viewModelScope.launch {

            val _token = getApplication<Application>().getToken()
            Log.d("respuesta", _token.toString())

            try {
                userrolstate.value = RolUserState.Loading
                val updateRolrequest = updateRolrequest(_id, _rol)

                val response = UsersServices.instance.updaterol(_token, updateRolrequest)
                userrolstate.value = RolUserState.Success(response) // Usa la lista de usuarios directamente

                Log.d("respuesta2", userrolstate.value.toString())


            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string() ?: "No error body"
                        val errorCode = e.code()

                        // Actualizar el estado con información más específica
                        userrolstate.value = RolUserState.Error("Error: HTTP $errorCode - ${e.message()}")

                        // Log más detallado
                        Log.e("Error-APT", "HTTP error: $errorCode - ${e.message()}")
                        Log.e("Error-APT", "Error body: $errorBody")
                    }
                    is IOException -> {
                        // Posible error de red (sin conexión, tiempo de espera agotado, etc.)
                        userrolstate.value = RolUserState.Error("Error: Network error - ${e.message}")
                        Log.e("Error-APT", "Network error: ${e.message}")
                    }
                    else -> {
                        // Cualquier otro tipo de error
                        userrolstate.value = RolUserState.Error("Error: ${e.message}")
                        Log.e("Error-APT", "An error has occurred: ${e.message}")
                    }
                }
            }
        }
        }

    fun allUsers() {
        viewModelScope.launch {
            val _token = getApplication<Application>().getToken()
            Log.d("respuesta", _token.toString())

            try {
                allusersState.value = AllUsersState.Loading

                val response = UsersServices.instance.allUsers(_token)
                allusersState.value = AllUsersState.Success(response) // Usa la lista de usuarios directamente

                Log.d("respuesta2", allusersState.value.toString())


            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string() ?: "No error body"
                        val errorCode = e.code()

                        // Actualizar el estado con información más específica
                        allusersState.value = AllUsersState.Error("Error: HTTP $errorCode - ${e.message()}")

                        // Log más detallado
                        Log.e("Error-APT", "HTTP error: $errorCode - ${e.message()}")
                        Log.e("Error-APT", "Error body: $errorBody")
                    }
                    is IOException -> {
                        // Posible error de red (sin conexión, tiempo de espera agotado, etc.)
                        allusersState.value = AllUsersState.Error("Error: Network error - ${e.message}")
                        Log.e("Error-APT", "Network error: ${e.message}")
                    }
                    else -> {
                        // Cualquier otro tipo de error
                        allusersState.value = AllUsersState.Error("Error: ${e.message}")
                        Log.e("Error-APT", "An error has occurred: ${e.message}")
                    }
                }
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

sealed class SignUpState{
    object Initial: SignUpState()
    object Loading: SignUpState()
    data class Success(val signupResponse: RegistrarResponse): SignUpState()
    data class Error (val message : String): SignUpState()
}
sealed class GetUserByIdState{
    object Initial: GetUserByIdState()
    object Loading: GetUserByIdState()
    data class Success(val response: User): GetUserByIdState()
    data class Error (val message : String): GetUserByIdState()
}

sealed class UpdateState{
    object Initial: UpdateState()
    object Loading: UpdateState()
    data class Success(val response: updateResponse): UpdateState()
    data class Error (val message : String): UpdateState()
}


sealed class AllUsersState{
    object Initial: AllUsersState()
    object Loading: AllUsersState()
    data class Success(val response: List<usersALL>): AllUsersState()
    data class Error (val message : String): AllUsersState()
}
sealed class RolUserState{
    object Initial: RolUserState()
    object Loading: RolUserState()
    data class Success(val response: updateResponse): RolUserState()
    data class Error (val message : String): RolUserState()
}


sealed class rol{
    object Initial: rol()
    object Loading: rol()
    data class Success(val response: String): rol()
    data class Error (val message : String): rol()
}

