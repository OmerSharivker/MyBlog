package com.example.myblog.data.repository

import android.content.Context
import android.net.Uri
import com.example.myblog.data.api.CloudinaryService
import com.example.myblog.data.api.FirebaseService
import com.example.myblog.data.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository(
    private val firebaseService: FirebaseService = FirebaseService(),
    private val cloudinaryService: CloudinaryService = CloudinaryService()
) {

    suspend fun uploadPostToFirestore(imageUri: Uri, description: String, context: Context, onResult: (Boolean, String?) -> Unit) {
        withContext(Dispatchers.IO) {

            cloudinaryService.uploadImage(imageUri, context) { success, imageUrl ->
                if (success && imageUrl != null) {
                    val post = Post(
                        id = firebaseService.generatePostId(),
                        userId = firebaseService.getCurrentUserId() ?: "Unknown User",
                        userName = "Current User Name",
                        userProfileImageUrl = "Current User Profile URL",
                        postImageUrl = imageUrl,
                        description = description
                    )

                    firebaseService.savePostToFirestore(post) { success, error ->
                        if (success) {
                            onResult(true, null)
                        } else {
                            onResult(false, error)
                        }
                    }
                } else {
                    onResult(false, "Failed to upload image to Cloudinary")
                }
            }
        }
    }

    suspend fun fetchPosts(onResult: (List<Post>) -> Unit) {
        firebaseService.getPosts { posts ->
            onResult(posts)
        }
    }

    suspend fun generatePostDescription(): String {
        return "Generated description for the post." // Mock AI service
    }
}
