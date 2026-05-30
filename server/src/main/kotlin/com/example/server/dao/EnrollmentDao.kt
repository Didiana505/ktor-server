package com.example.server.dao

import com.example.server.model.CourseResponse
import com.example.server.model.EnrollmentResponse
import com.example.server.model.TeacherResponse
import com.example.server.table.CoursesTable
import com.example.server.table.EnrollmentsTable
import com.example.server.table.TeachersTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

object EnrollmentDao {

    fun enrollUser(
        userId: Int,
        courseId: Int,
        teacherId: Int
    ): Boolean = transaction {
        val courseExists = CoursesTable
            .selectAll()
            .where { CoursesTable.id eq courseId }
            .count() > 0

        if (!courseExists) {
            return@transaction false
        }

        val teacherExists = TeachersTable
            .selectAll()
            .where {
                (TeachersTable.id eq teacherId) and
                        (TeachersTable.courseId eq courseId)
            }
            .count() > 0

        if (!teacherExists) {
            return@transaction false
        }

        EnrollmentsTable.insertIgnore {
            it[EnrollmentsTable.userId] = userId
            it[EnrollmentsTable.courseId] = courseId
            it[EnrollmentsTable.teacherId] = teacherId
            it[enrolledAt] = LocalDateTime.now().toString()
        }

        true
    }

    fun getUserEnrollments(userId: Int): List<EnrollmentResponse> = transaction {
        val query = EnrollmentsTable
            .innerJoin(CoursesTable)
            .selectAll()
            .where { EnrollmentsTable.userId eq userId }

        query.map { row ->
            row.toEnrollmentResponse()
        }
    }

    fun isUserEnrolled(userId: Int, courseId: Int): Boolean = transaction {
        EnrollmentsTable
            .selectAll()
            .where {
                (EnrollmentsTable.userId eq userId) and
                        (EnrollmentsTable.courseId eq courseId)
            }
            .count() > 0
    }

    fun cancelEnrollment(userId: Int, courseId: Int): Boolean = transaction {
        val deletedRows = EnrollmentsTable.deleteWhere {
            (EnrollmentsTable.userId eq userId) and
                    (EnrollmentsTable.courseId eq courseId)
        }

        deletedRows > 0
    }

    private fun ResultRow.toEnrollmentResponse(): EnrollmentResponse {
        val course = CourseResponse(
            id = this[CoursesTable.id],
            title = this[CoursesTable.title],
            description = this[CoursesTable.description],
            duration = this[CoursesTable.duration],
            category = this[CoursesTable.category]
        )

        val teacherId = this[EnrollmentsTable.teacherId]

        val teacher = teacherId?.let { id ->
            TeachersTable
                .selectAll()
                .where { TeachersTable.id eq id }
                .map { teacherRow ->
                    TeacherResponse(
                        id = teacherRow[TeachersTable.id],
                        fullName = teacherRow[TeachersTable.fullName],
                        position = teacherRow[TeachersTable.position],
                        courseId = teacherRow[TeachersTable.courseId]
                    )
                }
                .singleOrNull()
        }

        return EnrollmentResponse(
            id = this[EnrollmentsTable.id],
            userId = this[EnrollmentsTable.userId],
            course = course,
            teacher = teacher,
            enrolledAt = this[EnrollmentsTable.enrolledAt]
        )
    }
}