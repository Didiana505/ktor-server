package com.example.server.model

import kotlinx.serialization.Serializable

@Serializable
data class TeacherResponse(
    val id: Int,
    val fullName: String,
    val position: String,
    val courseId: Int
)