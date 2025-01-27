package com.example.myblog.ui.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myblog.data.model.Post
import com.example.myblog.data.model.User
import com.example.myblog.data.repository.PostRepository
import com.example.myblog.data.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository = ProfileRepository()) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user
    private val postRepository = PostRepository()
    private val _userPosts = MutableLiveData<List<Post>>()
    val userPosts: LiveData<List<Post>> get() = _userPosts

    fun loadProfile() {
        viewModelScope.launch {
            profileRepository.getCurrentUser { user ->
                _user.postValue(user)
                user?.let {
                    loadUserPosts(it.id)
                }
            }
        }
    }

    private fun loadUserPosts(userId: String) {
        viewModelScope.launch {
            profileRepository.getUserPosts(userId) { posts ->
                _userPosts.postValue(posts)
            }
        }
    }

    fun updateProfile(
        newName: String,
        newImageUri: Uri?,
        context: Context,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            profileRepository.updateUserProfile(newName, newImageUri, context) { success, error ->
                if (success) {
                    loadProfile() // Reload profile data
                }
                onResult(success, error) // העברת התוצאה ל-callback
            }
        }
    }
    fun updateUserPosts(userId: String, newName: String?, newProfileImageUrl: String?, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            profileRepository.updateUserPosts(userId, newName, newProfileImageUrl, onResult)
        }
    }
    fun listenToUserPosts(userId: String) {
        profileRepository.listenToUserPosts(userId) { posts ->
            _userPosts.postValue(posts)
        }
    }


    fun toggleLike(postId: String, liked: Boolean) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        viewModelScope.launch {
            postRepository.toggleLike(postId, liked) { success, error ->
                if (success) {
                    _userPosts.value = _userPosts.value?.map { post ->
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

