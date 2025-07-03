package com.example.user.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.user.database.models.UsersDbModel
import kotlinx.coroutines.flow.Flow


@Dao
internal interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UsersDbModel>) : List<Long>

    @Query("SELECT COUNT(*) FROM users WHERE id < :id ORDER BY id")
    suspend fun userOffset(id: Long) : Int

    @Query("SELECT * FROM users ORDER BY id")
    fun usersPagingSource(): PagingSource<Int, UsersDbModel>

    @Query("SELECT EXISTS(SELECT 1 FROM users)")
    suspend fun usersExist(): Boolean

    @Query("DELETE FROM users")
    suspend fun clearUsers()

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUser(id: Long): Flow<UsersDbModel?>
}