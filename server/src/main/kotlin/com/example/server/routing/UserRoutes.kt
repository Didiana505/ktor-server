package com.example.server.routing

import com.example.server.dao.UserDao
import com.example.server.model.UpdateAgeRequest
import com.example.server.security.getUidFromToken
import com.example.server.security.requireUid
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put

fun Route.userRoutes() {

    get("/users/me") {
        val firebaseUid = call.requireUid()

        val user = UserDao.findByFirebaseUid(firebaseUid)

        if (user == null) {
            call.respond(HttpStatusCode.NotFound, "Пользователь не найден")
        } else {
            call.respond(user)
        }
    }


    put("/users/me/age") {
        val firebaseUid = call.requireUid()
        val request = call.receive<UpdateAgeRequest>()

        if (request.age <= 0 || request.age > 120) {
            call.respond(HttpStatusCode.BadRequest, "Некорректный возраст")
            return@put
        }

        val updatedUser = UserDao.updateAge(
            firebaseUid = firebaseUid,
            email = request.email,
            firstName = request.firstName,
            lastName = request.lastName,
            age = request.age
        )

        call.respond(updatedUser)
    }
}