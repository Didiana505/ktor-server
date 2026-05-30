package com.example.server.security

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import java.io.InputStream

object FirebaseConfig {

    fun init() {
        if (FirebaseApp.getApps().isNotEmpty()) {
            return
        }

        try {
            val serviceAccount: InputStream = this::class.java
                .classLoader
                .getResourceAsStream("service-account-key.json")
                ?: throw IllegalStateException("service-account-key.json not found")

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

            FirebaseApp.initializeApp(options)
            println("Firebase Admin SDK initialized successfully")
        } catch (e: Exception) {
            println("Failed to initialize Firebase Admin SDK: ${e.message}")
            throw e
        }
    }

    fun getAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}