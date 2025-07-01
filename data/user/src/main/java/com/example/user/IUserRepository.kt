package com.example.user

interface IUserRepository {
    suspend fun getUsers(page: Int): List<User>
}