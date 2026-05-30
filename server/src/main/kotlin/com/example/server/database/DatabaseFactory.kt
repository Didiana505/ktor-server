package com.example.server.database

import org.jetbrains.exposed.sql.Database

object DatabaseFactory {

    fun init() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5433/online_courses",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "1234"
        )
    }
}