package com.example.user.database

import com.example.core.di.Dispatcher
import com.example.core.di.SHDispatchers
import com.example.user.database.models.RemoteKeysDbModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UserRemoteKeysDataBaseDataSource @Inject constructor(
    private val remoteKeyDao: RemoteKeysDao,
    @Dispatcher(SHDispatchers.IO)
    private val dispatcher: CoroutineDispatcher,
) {
    suspend fun insertAll(remoteKey: List<RemoteKeysDbModel>) = withContext(dispatcher){
        remoteKeyDao.insertAll(remoteKey)
    }

    suspend fun remoteKeysUserId(userId: Long): RemoteKeysDbModel? = withContext(dispatcher){
        remoteKeyDao.remoteKeysUserId(userId)
    }

    suspend fun clearRemoteKeys() = withContext(dispatcher){
        remoteKeyDao.clearRemoteKeys()
    }
}