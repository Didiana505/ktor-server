package com.example.server.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TeachersTable : Table("teachers") {
    val id = integer("id")

    val fullName = varchar("full_name", 255)

    val position = varchar("position", 255)

    val courseId = integer("course_id").references(
        CoursesTable.id,
        onDelete = ReferenceOption.RESTRICT
    )

    override val primaryKey = PrimaryKey(id)
}