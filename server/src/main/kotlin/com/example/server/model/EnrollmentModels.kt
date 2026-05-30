package com.example.server.model

import kotlinx.serialization.Serializable

@Serializable
data class EnrollRequest(
    val firebaseUid: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val courseId: Int,
    val teacherId: Int
)

@Serializable
data class EnrollmentResponse(
    val id: Int,
    val userId: Int,
    val course: CourseResponse,
    val teacher: TeacherResponse?,
    val enrolledAt: String
)