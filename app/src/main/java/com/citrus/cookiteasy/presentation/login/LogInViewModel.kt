package com.citrus.cookiteasy.presentation.login

import androidx.lifecycle.ViewModel
import com.citrus.cookiteasy.data.database.DatabaseRepository
import com.citrus.cookiteasy.data.database.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState


    sealed class LoginState{
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val user: User) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    suspend fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading
        try {
            val user = databaseRepository.getUserByUsername(username)

            when {
                user == null -> { _loginState.value = LoginState.Error("Пользователь с таким логином не найден")}
                user.password != password -> {
                    _loginState.value = LoginState.Error("Неверный пароль")
                }
                else -> {_loginState.value = LoginState.Success(user)
                }
            }

        }catch (e: Exception) {
            _loginState.value = LoginState.Error("Ошибка при входе: ${e.message}")
        }
    }


}