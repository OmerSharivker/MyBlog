package com.example.myblog.data.repository

import com.example.myblog.data.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PostRepository {

    private val firestore = FirebaseFirestore.getInstance()


    private val postsCollection = firestore.collection("posts")


    suspend fun addPost(post: Post): Result<Boolean> {
        return try {
            postsCollection.document(post.id).set(post).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fetch all posts from Firestore
    suspend fun fetchPosts(): Result<List<Post>> {
        return try {
            val snapshot = postsCollection.get().await()
            val posts = snapshot.toObjects(Post::class.java)
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Use AI to generate a description for the post
    suspend fun generateDescription(imageUrl: String): Result<String> {
        return try {
            // Call the Gemini (or OpenAI) API to generate the description
            val description = callGeminiAPI(imageUrl)
            Result.success(description)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Simulating the Gemini API call (replace this with real implementation)
    private suspend fun callGeminiAPI(imageUrl: String): String {
        // Simulate API logic (use Retrofit/HTTP for real implementation)
        return "Generated description for image: $imageUrl"
    }
}