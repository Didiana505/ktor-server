package com.example.server.dao

import com.example.server.model.CourseResponse
import com.example.server.table.CoursesTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object CourseDao {

    fun getAllCourses(): List<CourseResponse> = transaction {
        CoursesTable
            .selectAll()
            .map { row -> row.toCourseResponse() }
    }

    fun getCourseById(id: Int): CourseResponse? = transaction {
        CoursesTable
            .selectAll()
            .where { CoursesTable.id eq id }
            .map { row -> row.toCourseResponse() }
            .singleOrNull()
    }

    private fun ResultRow.toCourseResponse(): CourseResponse {
        return CourseResponse(
            id = this[CoursesTable.id],
            title = this[CoursesTable.title],
            description = this[CoursesTable.description],
            duration = this[CoursesTable.duration],
            category = this[CoursesTable.category]
        )
    }
}