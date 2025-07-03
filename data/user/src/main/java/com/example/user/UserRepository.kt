package com.example.user

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.user.api.UserRemoteDataSource
import com.example.user.database.SHDataBase
import com.example.user.database.UserDataBaseDataSource
import com.example.user.database.UserRemoteKeysDataBaseDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserRepository @Inject constructor(
    private val apiService: UserRemoteDataSource,
    private val userDBDataStore: UserDataBaseDataSource,
    private val remoteKeyDBDataStore: UserRemoteKeysDataBaseDataSource,
    private val dataBase: SHDataBase,
) : IUserRepository {
    override suspend fun getUsers(firstUserId: Long?): Flow<PagingData<User>> {
        val pagingSourceFactory = { userDBDataStore.usersPagingSource() }

        val initialOffset = if(firstUserId == null){
            null
        }
        else{
            userDBDataStore.userOffset(firstUserId)
        }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
            ),
            remoteMediator = UserRemoteMediator(
                apiService,
                userDBDataStore,
                remoteKeyDBDataStore,
                dataBase
            ),
            initialKey = initialOffset,
            pagingSourceFactory = pagingSourceFactory
        ).flow
         .map { pagingData ->
             pagingData.map { it.toBusinessModel() }
        }
    }

    override fun getUser(id: Long): Flow<User?>{
        return userDBDataStore.getUser(id).map{
            it?.let{ it.toBusinessModel() }
        }
    }
}
