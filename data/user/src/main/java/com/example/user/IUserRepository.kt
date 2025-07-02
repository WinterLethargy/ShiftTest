package com.example.user

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.user.database.UsersDbModel
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUsers(initialOffset: Int?): Flow<PagingData<User>>
}