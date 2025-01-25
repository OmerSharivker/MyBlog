package com.example.myblog.data.api

import com.example.myblog.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.myblog.data.model.Post

class FirebaseService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()



    fun generatePostId(): String {
        return firestore.collection("posts").document().id
    }


    fun savePostToFirestore(post: Post, onResult: (Boolean, String?) -> Unit) {
        Log.d("FirebaseService", "Saving post to Firestore: ${post.id}")
        firestore.collection("posts").document(post.id)
            .set(post)
            .addOnSuccessListener {
                Log.d("FirebaseService", "Post saved successfully")
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseService", "Failed to save post: ${e.message}")
                onResult(false, e.message)
            }
    }


    fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun registerUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
    fun saveUserToFirestore(user: User, onResult: (Boolean, String?) -> Unit) {
        Log.d("FirebaseService", "Saving user to Firestore: ${user.id}")
        firestore.collection("users").document(user.id)
            .set(user)
            .addOnSuccessListener {
                Log.d("FirebaseService", "User saved successfully")
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseService", "Failed to save user: ${e.message}")
                onResult(false, e.message)
            }
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
    fun getUserById(userId: String, onResult: (User?) -> Unit) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
    fun getPosts(onResult: (List<Post>) -> Unit) {
        firestore.collection("posts")
            .get()
            .addOnSuccessListener { snapshot ->
                val posts = snapshot.toObjects(Post::class.java)
                onResult(posts)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }
    fun getPostById(postId: String, onResult: (Post?) -> Unit) {
        firestore.collection("posts").document(postId).get()
            .addOnSuccessListener { document ->
                val post = document.toObject(Post::class.java)
                onResult(post)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun updatePostLikes(postId: String, likes: List<Any>, onResult: (Boolean, String?) -> Unit) {
        firestore.collection("posts").document(postId)
            .update("likes", likes)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }



}