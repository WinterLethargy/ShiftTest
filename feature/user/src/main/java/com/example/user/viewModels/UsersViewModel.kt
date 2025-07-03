package com.example.user.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.user.IUserRepository
import com.example.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
internal class UsersViewModel @Inject constructor(
    private val userRepository : IUserRepository,
) : ViewModel() {
    suspend fun getPagingDataFlow(firstUserId: Long?): Flow<PagingData<User>> = userRepository
        .getUsers(firstUserId)
        .cachedIn(viewModelScope)
}