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

    @Query("SELECT * FROM users")
    fun users(): PagingSource<Int, UsersDbModel>

    @Query("DELETE FROM users")
    suspend fun clearUsers()
}