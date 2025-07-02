package com.example.user.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<UsersDbModel>) : List<Long>

    @Query("SELECT * FROM users ORDER BY id")
    fun usersPagingSource(): PagingSource<Int, UsersDbModel>

    @Query("SELECT EXISTS(SELECT 1 FROM users)")
    suspend fun usersExist(): Boolean

    @Query("DELETE FROM users")
    suspend fun clearUsers()
}