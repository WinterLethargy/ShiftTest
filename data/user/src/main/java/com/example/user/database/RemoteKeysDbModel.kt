package com.example.user.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysDbModel(
    @PrimaryKey val userId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)