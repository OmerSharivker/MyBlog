package com.example.myblog.ui.post.create


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myblog.data.repository.PostRepository
import kotlinx.coroutines.launch

class CreatePostViewModel : ViewModel() {

    private val repository = PostRepository()

    fun uploadPost(imageUri: Uri, description: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            repository.uploadPostToFirestore(imageUri, description, onResult)
        }
    }

    fun generateDescription(onResult: (String) -> Unit) {
        // קריאה לשירות AI
        viewModelScope.launch {
            val generatedText = repository.generatePostDescription()
            onResult(generatedText)
        }
    }
}