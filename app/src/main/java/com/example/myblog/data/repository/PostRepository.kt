package com.example.myblog.data.repository

import android.content.Context
import android.net.Uri

import com.example.myblog.data.api.CloudinaryService
import com.example.myblog.data.api.FirebaseService
import com.example.myblog.data.api.GeminiService
import com.example.myblog.data.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PostRepository(
    private val firebaseService: FirebaseService = FirebaseService(),
    private val cloudinaryService: CloudinaryService = CloudinaryService(),
    private val geminiService: GeminiService = GeminiService()
) {

    suspend fun uploadPostToFirestore(imageUri: Uri, description: String, context: Context, onResult: (Boolean, String?) -> Unit) {
        withContext(Dispatchers.IO) {
            cloudinaryService.uploadImage(imageUri, context) { success, imageUrl ->
                if (success && imageUrl != null) {
                    val currentUserId = firebaseService.getCurrentUserId() ?: "Unknown User"

                    firebaseService.getUserById(currentUserId) { user ->
                        val post = Post(
                            id = firebaseService.generatePostId(),
                            userId = currentUserId,
                            userName = user?.name ?: "Current User",
                            userProfileImageUrl = user?.profileImageUrl ?: "android.resource://com.example.myblog/drawable/ic_profile_placeholder",
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
        return geminiService.generateFunnySentence()
    }


}
