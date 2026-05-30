package com.example.server.routing

import com.example.server.dao.TeacherDao
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.teacherRoutes() {

    get("/courses/{courseId}/teachers") {
        val courseId = call.parameters["courseId"]?.toIntOrNull()

        if (courseId == null) {
            call.respond(HttpStatusCode.BadRequest, "Некорректный id курса")
            return@get
        }

        val teachers = TeacherDao.getTeachersByCourseId(courseId)

        call.respond(teachers)
    }
}