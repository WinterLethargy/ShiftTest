package com.example.user

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.ExperimentalPagingApi
import androidx.room.withTransaction
import com.example.user.api.UserApiModel
import com.example.user.database.RemoteKeysDbModel
import com.example.user.database.SHDataBase
import com.example.user.database.UsersDbModel
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import java.io.IOException
import com.example.user.api.UserRemoteDataSource

private const val START_PAGE_INDEX = 0
private const val END_PAGE_INDEX = 10

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator (
    private val apiService: UserRemoteDataSource,
    private val repoDatabase: SHDataBase
) : RemoteMediator<Int, UsersDbModel>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, UsersDbModel>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: START_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val users = apiService.fetchUsers(page)
            val endOfPaginationReached = page == END_PAGE_INDEX
            repoDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    repoDatabase.remoteKeysDao().clearRemoteKeys()
                    repoDatabase.usersDao().clearUsers()
                }
                val prevKey = if (page == START_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val dbModels = users.map(UserApiModel::toDbModel)
                var ids = repoDatabase.usersDao().insertAll(dbModels)
                val dbModelsWithIds = dbModels.zip(ids) { db, id -> db.copy(id = id) }
                val keys = dbModelsWithIds.map {
                    RemoteKeysDbModel(userId = it.id!!, prevKey = prevKey, nextKey = nextKey)
                }
                repoDatabase.remoteKeysDao().insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UsersDbModel>): RemoteKeysDbModel? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { user ->
                repoDatabase.remoteKeysDao().remoteKeysUserId(user.id!!)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UsersDbModel>): RemoteKeysDbModel? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                repoDatabase.remoteKeysDao().remoteKeysUserId(repo.id!!)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UsersDbModel>
    ): RemoteKeysDbModel? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { userId ->
                repoDatabase.remoteKeysDao().remoteKeysUserId(userId)
            }
        }
    }
}