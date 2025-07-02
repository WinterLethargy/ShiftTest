package com.example.user.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UsersDbModel(
    @PrimaryKey(autoGenerate = true)
    val id : Long?,
    val gender: String,
    val title: String,
    val first: String,
    val last: String,
    val city: String,
    val country: String,
    val email: String,
    val date: String,
    val age: Int,
    val phone: String,
    val largePic: String,
    val mediumPic: String,
    val thumbnailPic: String
)
