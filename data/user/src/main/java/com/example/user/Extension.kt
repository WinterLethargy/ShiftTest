package com.example.user

import com.example.user.api.UserApiModel
import com.example.user.database.models.UsersDbModel

internal fun UserApiModel.toDbModel(): UsersDbModel = UsersDbModel(
    id = null,
    gender = gender,
    title = name.title,
    first = name.first,
    last = name.last,
    city = location.city,
    country = location.country,
    latitude = location.coordinates.latitude,
    longitude = location.coordinates.longitude,
    email = email,
    date = dob.date,
    age = dob.age,
    phone = phone,
    largePic = picture.large,
    mediumPic = picture.medium,
    thumbnailPic = picture.thumbnail
)

internal fun UsersDbModel.toBusinessModel(): User = User(
    id = id ?: 0,
    gender = gender,
    name = User.Name(
        title = title,
        first = first,
        last = last
    ),
    location = User.Location(
        city = city,
        country = country,
        coordinates = User.Location.Coordinates(
            latitude = latitude,
            longitude = longitude,
        )
    ),
    email = email,
    dob = User.Dob(
        date = date,
        age = age
    ),
    phone = phone,
    picture = User.Picture(
        large = largePic,
        medium = mediumPic,
        thumbnail = thumbnailPic
    )
)