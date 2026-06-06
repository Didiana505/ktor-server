package com.example.server.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val firebaseUid: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val age: Int? = null
)

@Serializable
data class UpdateAgeRequest(
    val email: String,
    val firstName: String,
    val lastName: String,
    val age: Int
)@Serializable
data class CreateUserRequest(
    val email: String,
    val firstName: String,
    val lastName: String
)