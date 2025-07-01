package com.example.user.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.user.IUserRepository
import com.example.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository : IUserRepository,
) : ViewModel() {
    suspend fun fetchUsers() : Unit{
        viewModelScope.launch {
            val users = userRepository.getUsers(1)
            Log.i("UsersViewModel", users.toString())
        }
    }
}