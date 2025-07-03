package com.example.user.api

import com.example.core.di.Dispatcher
import com.example.core.di.SHDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UserRemoteDataSource @Inject constructor(
    private val api: UserApiService,
    @Dispatcher(SHDispatchers.IO)
    private val dispatcher: CoroutineDispatcher,
) {
    suspend fun fetchUsers(page: Int): List<UserApiModel> = withContext(dispatcher) {
        api.getUsers(page = page).results
    }
}