package com.example.myblog.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myblog.data.model.Post
import com.example.myblog.data.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val postRepository = PostRepository()

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    fun loadPosts() {
        viewModelScope.launch {
            postRepository.fetchPosts { postList ->
                _posts.value = postList
            }
        }
    }

    fun toggleLike(postId: String, liked: Boolean) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            postRepository.toggleLike(postId, liked) { success, error ->
                if (success) {
                    _posts.value = _posts.value?.map { post ->
                        if (post.id == postId) {
                            val updatedLikes = if (liked) {
                                if (!post.likes.contains(currentUserId)) {
                                    post.likes + currentUserId
                                } else post.likes
                            } else {
                                post.likes - currentUserId
                            }
                            post.copy(likes = updatedLikes)
                        } else post
                    }
                } else {
                    // Error handling
                }
            }
        }
    }
}