package com.example.server.routing


import com.example.server.dao.CourseDao
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.courseRoutes() {

    get("/") {
        call.respond(
            mapOf("message" to "Online Courses Ktor Server is running")
        )
    }

    get("/courses") {
        val courses = CourseDao.getAllCourses()
        call.respond(courses)
    }

    get("/courses/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()

        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, "Некорректный id курса")
            return@get
        }

        val course = CourseDao.getCourseById(id)

        if (course == null) {
            call.respond(HttpStatusCode.NotFound, "Курс не найден")
        } else {
            call.respond(course)
        }
    }
}