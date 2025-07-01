package com.example.user

import com.example.user.api.UserRemoteDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
) : IUserRepository {
    override suspend fun getUsers(page: Int): List<User> {
        return remoteDataSource.fetchUsers(page).map { apiModel ->
            User(
                gender = apiModel.gender,
                name = User.Name(
                    title = apiModel.name.title,
                    first = apiModel.name.first,
                    last = apiModel.name.last,
                ),
                location = User.Location(
                    city = apiModel.location.city,
                    country = apiModel.location.country
                ),
                email = apiModel.email,
                dob = User.Dob(
                    date = apiModel.dob.date,
                    age = apiModel.dob.age,
                ),
                phone = apiModel.phone,
                picture = User.Picture(
                    large = apiModel.picture.large,
                    medium = apiModel.picture.medium,
                    thumbnail = apiModel.picture.thumbnail
                )
            )
        }
    }
}
