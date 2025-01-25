package com.example.myblog.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myblog.data.model.Post
import com.example.myblog.data.repository.PostRepository
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
}