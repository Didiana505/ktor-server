package com.example.server.table

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = integer("id").autoIncrement()
    val firebaseUid = varchar("firebase_uid", 128).uniqueIndex()
    val email = varchar("email", 255)

    val firstName = varchar("first_name", 100)
    val lastName = varchar("last_name", 100)

    val age = integer("age").nullable()

    override val primaryKey = PrimaryKey(id)
}