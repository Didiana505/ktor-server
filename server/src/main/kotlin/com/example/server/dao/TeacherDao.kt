package com.example.server.dao

import com.example.server.model.TeacherResponse
import com.example.server.table.TeachersTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object TeacherDao {

    fun getTeachersByCourseId(courseId: Int): List<TeacherResponse> = transaction {
        TeachersTable
            .selectAll()
            .where { TeachersTable.courseId eq courseId }
            .map { row -> row.toTeacherResponse() }
    }

    fun getTeacherById(id: Int): TeacherResponse? = transaction {
        TeachersTable
            .selectAll()
            .where { TeachersTable.id eq id }
            .map { row -> row.toTeacherResponse() }
            .singleOrNull()
    }

    private fun ResultRow.toTeacherResponse(): TeacherResponse {
        return TeacherResponse(
            id = this[TeachersTable.id],
            fullName = this[TeachersTable.fullName],
            position = this[TeachersTable.position],
            courseId = this[TeachersTable.courseId]
        )
    }
}