package com.example.server.security

import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.http.HttpStatusCode

class UnauthorizedException(message: String) : Exception(message)// Исключение для случаев токен отсутствует или невалиден

suspend fun ApplicationCall.getUidFromToken(): String? {
    val authHeader = request.headers["Authorization"]
    val token = authHeader?.removePrefix("Bearer ")?.trim()

    if (token.isNullOrBlank()) {
        return null
    }

    return try {
        val decodedToken = FirebaseConfig.getAuth().verifyIdToken(token) // проверка токена через Firebase
        decodedToken.uid
    } catch (e: Exception) {
        println("Token verification failed: ${e.message}")
        null
    }
}


suspend fun ApplicationCall.requireUid(): String { // возвращает uid, иначе отправляет 401
    val uid = getUidFromToken()
    if (uid == null) {
        respond(HttpStatusCode.Unauthorized, "Unauthorized: Invalid or missing token")
        throw UnauthorizedException("Invalid token")
    }
    return uid
}