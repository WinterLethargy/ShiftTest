package com.example.user

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getUsers(firstUserId: Long?): Flow<PagingData<User>>
    fun getUser(id: Long): Flow<User?>
}