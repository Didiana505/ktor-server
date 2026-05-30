package com.example.server.dao

import com.example.server.model.UserResponse
import com.example.server.table.UsersTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object UserDao {

    fun findByFirebaseUid(firebaseUid: String): UserResponse? = transaction {
        UsersTable
            .selectAll()
            .where { UsersTable.firebaseUid eq firebaseUid }
            .map { row -> row.toUserResponse() }
            .singleOrNull()
    }

    fun getOrCreateUser(
        firebaseUid: String,
        email: String,
        firstName: String,
        lastName: String
    ): UserResponse = transaction {
        val existingUser = UsersTable
            .selectAll()
            .where { UsersTable.firebaseUid eq firebaseUid }
            .map { row -> row.toUserResponse() }
            .singleOrNull()

        if (existingUser != null) {
            UsersTable.update({ UsersTable.firebaseUid eq firebaseUid }) {
                it[UsersTable.email] = email
                it[UsersTable.firstName] = firstName
                it[UsersTable.lastName] = lastName
            }

            existingUser.copy(
                email = email,
                firstName = firstName,
                lastName = lastName
            )
        } else {
            val newId = UsersTable.insert {
                it[UsersTable.firebaseUid] = firebaseUid
                it[UsersTable.email] = email
                it[UsersTable.firstName] = firstName
                it[UsersTable.lastName] = lastName
                it[UsersTable.age] = null
            } get UsersTable.id

            UserResponse(
                id = newId,
                firebaseUid = firebaseUid,
                email = email,
                firstName = firstName,
                lastName = lastName,
                age = null
            )
        }
    }

    fun updateAge(
        firebaseUid: String,
        email: String,
        firstName: String,
        lastName: String,
        age: Int
    ): UserResponse = transaction {
        val existingUser = UsersTable
            .selectAll()
            .where { UsersTable.firebaseUid eq firebaseUid }
            .map { row -> row.toUserResponse() }
            .singleOrNull()

        if (existingUser != null) {
            UsersTable.update({ UsersTable.firebaseUid eq firebaseUid }) {
                it[UsersTable.email] = email
                it[UsersTable.firstName] = firstName
                it[UsersTable.lastName] = lastName
                it[UsersTable.age] = age
            }

            existingUser.copy(
                email = email,
                firstName = firstName,
                lastName = lastName,
                age = age
            )
        } else {
            val newId = UsersTable.insert {
                it[UsersTable.firebaseUid] = firebaseUid
                it[UsersTable.email] = email
                it[UsersTable.firstName] = firstName
                it[UsersTable.lastName] = lastName
                it[UsersTable.age] = age
            } get UsersTable.id

            UserResponse(
                id = newId,
                firebaseUid = firebaseUid,
                email = email,
                firstName = firstName,
                lastName = lastName,
                age = age
            )
        }
    }

    private fun ResultRow.toUserResponse(): UserResponse {
        return UserResponse(
            id = this[UsersTable.id],
            firebaseUid = this[UsersTable.firebaseUid],
            email = this[UsersTable.email],
            firstName = this[UsersTable.firstName],
            lastName = this[UsersTable.lastName],
            age = this[UsersTable.age]
        )
    }
}