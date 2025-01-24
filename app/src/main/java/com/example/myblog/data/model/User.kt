package com.example.myblog.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String = "android.resource://com.example.myblog/drawable/ic_profile_placeholder" // תמונת ברירת מחדל
)