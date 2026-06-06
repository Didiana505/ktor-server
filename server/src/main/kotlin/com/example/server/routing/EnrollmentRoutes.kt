package com.example.server.routing

import com.example.server.dao.EnrollmentDao
import com.example.server.dao.UserDao
import com.example.server.model.EnrollRequest
import com.example.server.security.requireUid
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.enrollmentRoutes() {


    post("/enrollments/secure") { //запись на курс
        val tokenUid = call.requireUid()
        val request = call.receive<EnrollRequest>() //читаем тело запроса


        val user = UserDao.getOrCreateUser(
            firebaseUid = tokenUid,
            email = request.email,
            firstName = request.firstName,
            lastName = request.lastName
        )

        val alreadyEnrolled = EnrollmentDao.isUserEnrolled(
            userId = user.id,
            courseId = request.courseId
        )

        if (alreadyEnrolled) {
            call.respond(
                HttpStatusCode.Conflict,
                "Пользователь уже записан на этот курс"
            )
            return@post
        }

        val success = EnrollmentDao.enrollUser(
            userId = user.id,
            courseId = request.courseId,
            teacherId = request.teacherId
        )

        if (success) {
            call.respond(HttpStatusCode.Created, "Запись на курс выполнена")
        } else {
            call.respond(HttpStatusCode.NotFound, "Курс или преподаватель не найден")
        }
    }

    get("/enrollments/me") { //получить мои записи
        val firebaseUid = call.requireUid()

        val user = UserDao.findByFirebaseUid(firebaseUid) //поиск в бд

        if (user == null) {
            call.respond(emptyList<Any>())
            return@get
        }

        val enrollments = EnrollmentDao.getUserEnrollments(user.id)
        call.respond(enrollments)
    }


    delete("/enrollments/me/{courseId}") {
        val firebaseUid = call.requireUid()
        val courseId = call.parameters["courseId"]?.toIntOrNull()

        if (courseId == null) {
            call.respond(HttpStatusCode.BadRequest, "Некорректный id курса")
            return@delete
        }

        val user = UserDao.findByFirebaseUid(firebaseUid)

        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "Пользователь не найден")
            return@delete
        }

        val success = EnrollmentDao.cancelEnrollment(
            userId = user.id,
            courseId = courseId
        )

        if (success) {
            call.respond(HttpStatusCode.OK, "Запись на курс отменена")
        } else {
            call.respond(HttpStatusCode.NotFound, "Запись на курс не найдена")
        }
    }

}