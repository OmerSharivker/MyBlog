package com.example.myblog.data.repository

import com.example.myblog.data.api.FirebaseService
import com.example.myblog.data.model.User

class AuthRepository(private val firebaseService: FirebaseService = FirebaseService()) {

    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseService.loginUser(email, password, onResult)
    }

    fun registerUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseService.registerUser(email, password, onResult)
    }

    fun saveUserToFirestore(userId: String, name: String, email: String, onResult: (Boolean, String?) -> Unit) {
        val user = User(id = userId, name = name, email = email)
        firebaseService.saveUserToFirestore(user, onResult)
    }

    fun getCurrentUserId(): String? {
        return firebaseService.getCurrentUserId()
    }
}