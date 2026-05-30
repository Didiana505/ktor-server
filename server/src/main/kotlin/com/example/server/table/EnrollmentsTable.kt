package com.example.server.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object EnrollmentsTable : Table("enrollments") {
    val id = integer("id").autoIncrement()

    val userId = integer("user_id").references(
        UsersTable.id,
        onDelete = ReferenceOption.CASCADE
    )

    val courseId = integer("course_id").references(
        CoursesTable.id,
        onDelete = ReferenceOption.CASCADE
    )

    val teacherId = optReference(
        name = "teacher_id",
        refColumn = TeachersTable.id,
        onDelete = ReferenceOption.SET_NULL
    )

    val enrolledAt = varchar("enrolled_at", 50)

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(userId, courseId)
    }
}