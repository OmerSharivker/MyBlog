package com.example.myblog.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myblog.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository()

    fun registerUser(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            repository.registerUser(email, password) { success, error ->
                if (success) {
                    val userId = repository.getCurrentUserId()//check if user successfully registered
                    if (userId != null) {
                        repository.saveUserToFirestore(userId, name, email) { saveSuccess, saveError ->
                            onResult(saveSuccess, saveError)
                        }
                    } else {
                        onResult(false, "User ID not found")
                    }
                } else {
                    onResult(false, error)
                }
            }
        }
    }
}