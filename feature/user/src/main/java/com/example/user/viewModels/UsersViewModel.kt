package com.example.user.viewModels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.user.IUserRepository
import com.example.user.User
import com.example.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userRepository : IUserRepository,
) : ViewModel() {
    suspend fun getPagingDataFlow(firstUserId: Long?): Flow<PagingData<User>> = userRepository
        .getUsers(firstUserId)
        .cachedIn(viewModelScope)
}