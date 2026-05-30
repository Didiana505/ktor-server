package com.example.server.table

import org.jetbrains.exposed.sql.Table

object CoursesTable : Table("courses") {
    val id = integer("id")
    val title = varchar("title", 255)
    val description = text("description")
    val duration = varchar("duration", 100)
    val category = varchar("category", 100)

    override val primaryKey = PrimaryKey(id)
}