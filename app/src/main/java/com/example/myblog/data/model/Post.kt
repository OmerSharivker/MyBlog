package com.example.myblog.data.model

data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImageUrl: String = "",
    val postImageUrl: String = "",
    val description: String = "",
    val likes: Int = 0,
    val comments: Int = 0
)