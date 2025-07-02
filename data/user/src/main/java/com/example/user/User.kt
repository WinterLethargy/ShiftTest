package com.example.user

data class User(
    val id: Long,
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val dob: Dob,
    val phone: String,
    val picture: Picture
) {
    data class Name(val title: String, val first: String, val last: String)
    data class Location(val city: String, val country: String)
    data class Dob(val date: String, val age: Int)
    data class Picture(val large: String, val medium: String, val thumbnail: String)
}