package com.example.user.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel  @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
}