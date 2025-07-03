package com.example.user.database

import androidx.paging.PagingSource
import com.example.core.di.Dispatcher
import com.example.core.di.SHDispatchers
import com.example.user.database.models.RemoteKeysDbModel
import com.example.user.database.models.UsersDbModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class UserDataBaseDataSource @Inject constructor(
    private val usersDao: UsersDao,
    @Dispatcher(SHDispatchers.IO)
    private val dispatcher: CoroutineDispatcher,
) {

    suspend fun insertAll(users: List<UsersDbModel>) : List<Long> = withContext(dispatcher){
        usersDao.insertAll(users)
    }

    suspend fun userOffset(id: Long) : Int = withContext(dispatcher){
        usersDao.userOffset(id)
    }

    fun usersPagingSource(): PagingSource<Int, UsersDbModel>{
        return usersDao.usersPagingSource()
    }

    suspend fun usersExist(): Boolean = withContext(dispatcher){
        usersDao.usersExist()
    }

    suspend fun clearUsers() = withContext(dispatcher){
        usersDao.clearUsers()
    }

    fun getUser(id: Long): Flow<UsersDbModel?>{
       return usersDao.getUser(id)
    }
}