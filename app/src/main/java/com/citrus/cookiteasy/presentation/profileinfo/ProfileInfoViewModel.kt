package com.citrus.cookiteasy.presentation.profileinfo

import androidx.lifecycle.ViewModel
import com.citrus.cookiteasy.data.database.DatabaseRepository
import com.citrus.cookiteasy.data.database.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileInfoViewModel @Inject constructor(
    private val repository: DatabaseRepository
) : ViewModel() {

    suspend fun updateUser(user: User) {
        // Сначала проверяем существование пользователя
        val existingUser = repository.getUserByUsername(user.username)

        if (existingUser != null) {
            // Обновляем только профильные данные, сохраняя пароль
            val updatedUser = existingUser.copy(
                firstName = user.firstName,
                lastName = user.lastName,
                birthDate = user.birthDate,
                height = user.height,
                weight = user.weight,
                bmi = user.bmi,
                gender = user.gender
            )
            repository.updateUser(updatedUser)
        } else {
            // Создаём нового пользователя (если вдруг не найден)
            repository.insertUser(user)
        }
    }
}