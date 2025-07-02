package com.example.user.api

import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {
    @GET("api/")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("results") results: Int = 25,
        @Query("seed") seed: String = "shift"
    ): UserResponse
}