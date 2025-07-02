package com.example.user

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.user.database.UsersDbModel
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getUsers(firstUserId: Long?): Flow<PagingData<User>>
}