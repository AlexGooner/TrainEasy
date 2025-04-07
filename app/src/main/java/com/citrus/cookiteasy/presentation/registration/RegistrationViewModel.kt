package com.citrus.cookiteasy.presentation.registration

import androidx.lifecycle.ViewModel
import com.citrus.cookiteasy.data.database.DatabaseRepository
import com.citrus.cookiteasy.data.database.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState


    sealed class RegistrationState {
        object Idle : RegistrationState()
        object Loading : RegistrationState()
        data class Success(val username: String) : RegistrationState()
        data class Error(val message: String) : RegistrationState()
    }



    suspend fun registerUser(username: String, password: String) {
        _registrationState.value = RegistrationState.Loading
        try {
            val existingUser = databaseRepository.getUserByUsername(username)

            if (existingUser != null) {
                _registrationState.value = RegistrationState.Error("Пользователь с таким именем уже существует")
            } else {
                // Создаем нового пользователя (остальные поля можно заполнить позже)
                val newUser = User(
                    username = username,
                    password = password,
                    firstName = "",
                    lastName = "",
                    birthDate = "",
                    height = 0,
                    weight = 0,
                    bmi = 0f,
                    gender = ""
                )

                databaseRepository.insertUser(newUser)
                _registrationState.value = RegistrationState.Success(username)
            }
        } catch (e: Exception) {
            _registrationState.value = RegistrationState.Error("Ошибка регистрации: ${e.message}")
        }
    }
}