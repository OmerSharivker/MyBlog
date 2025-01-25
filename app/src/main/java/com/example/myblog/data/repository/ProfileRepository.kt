package com.example.myblog.data.repository

import com.example.myblog.data.api.FirebaseService
import com.example.myblog.data.model.Post
import com.example.myblog.data.model.User

class ProfileRepository(private val firebaseService: FirebaseService = FirebaseService()) {

    fun getCurrentUser(onResult: (User?) -> Unit) {
        val currentUserId = firebaseService.getCurrentUserId()
        if (currentUserId != null) {
            firebaseService.getUserById(currentUserId) { user ->
                onResult(user)
            }
        } else {
            onResult(null)
        }
    }

    fun getUserPosts(userId: String, onResult: (List<Post>) -> Unit) {
        firebaseService.getPosts { posts ->
            val userPosts = posts.filter { it.userId == userId }
            onResult(userPosts)
        }
    }
}