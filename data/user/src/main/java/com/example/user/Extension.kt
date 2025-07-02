package com.example.user

import com.example.user.api.UserApiModel
import com.example.user.database.UsersDbModel

fun UserApiModel.toDbModel(): UsersDbModel = UsersDbModel(
    id = null,
    gender = gender,
    title = name.title,
    first = name.first,
    last = name.last,
    city = location.city,
    country = location.country,
    email = email,
    date = dob.date,
    age = dob.age,
    phone = phone,
    largePic = picture.large,
    mediumPic = picture.medium,
    thumbnailPic = picture.thumbnail
)

fun UsersDbModel.toBusinessModel(): User = User(
    id = id ?: 0,
    gender = gender,
    name = User.Name(
        title = title,
        first = first,
        last = last
    ),
    location = User.Location(
        city = city,
        country = country
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