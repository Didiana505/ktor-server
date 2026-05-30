package com.example.server.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseResponse(
    val id: Int,
    val title: String,
    val description: String,
    val duration: String,
    val category: String
)