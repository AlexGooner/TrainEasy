package com.citrus.cookiteasy.presentation.registration

import androidx.lifecycle.ViewModel
import com.citrus.cookiteasy.data.database.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {


}