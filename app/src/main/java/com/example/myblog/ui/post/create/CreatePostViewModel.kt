package com.example.myblog.ui.post.create

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myblog.data.repository.PostRepository
import kotlinx.coroutines.launch

class CreatePostViewModel : ViewModel() {

    private val repository = PostRepository()

    fun uploadPost(imageUri: Uri, description: String, context: Context, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            repository.uploadPostToFirestore(imageUri, description, context, onResult)
        }
    }

    fun generateDescription(onResult: (String) -> Unit) {
        viewModelScope.launch {
            val generatedText = repository.generatePostDescription()
            onResult(generatedText)
        }
    }
}