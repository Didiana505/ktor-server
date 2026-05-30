package com.example.server

import com.example.server.database.DatabaseFactory
import com.example.server.routing.courseRoutes
import com.example.server.routing.enrollmentRoutes
import com.example.server.routing.teacherRoutes
import com.example.server.routing.userRoutes
import com.example.server.security.FirebaseConfig  // ← НОВЫЙ ИМПОРТ
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun Application.module() {

    DatabaseFactory.init()

    FirebaseConfig.init()

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }
        )
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Put)
    }

    routing {
        courseRoutes()
        enrollmentRoutes()
        teacherRoutes()
        userRoutes()
    }
}