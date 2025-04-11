package com.citrus.cookiteasy.presentation.home

import androidx.lifecycle.ViewModel
import com.citrus.cookiteasy.data.database.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {



}