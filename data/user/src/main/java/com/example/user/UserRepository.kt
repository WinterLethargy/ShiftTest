package com.example.user

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.user.api.UserRemoteDataSource
import com.example.user.database.SHDataBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: UserRemoteDataSource,
    private val repoDatabase: SHDataBase
) : IUserRepository {
    override suspend fun getUsers(firstUserId: Long?): Flow<PagingData<User>> {
        val pagingSourceFactory = { repoDatabase.usersDao().usersPagingSource() }

        val initialOffset = if(firstUserId == null){
            null
        }
        else{
            repoDatabase.usersDao().userOffset(firstUserId)
        }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
            ),
            remoteMediator = UserRemoteMediator(
                apiService,
                repoDatabase
            ),
            initialKey = initialOffset,
            pagingSourceFactory = pagingSourceFactory
        ).flow
         .map { pagingData ->
             pagingData.map { it.toBusinessModel() }
        }
    }
}
