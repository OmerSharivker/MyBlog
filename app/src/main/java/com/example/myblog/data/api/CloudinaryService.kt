package com.example.myblog.data.api

import android.content.Context
import android.net.Uri
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.myblog.BuildConfig
import java.io.File

class CloudinaryService {

    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to BuildConfig.CLOUD_NAME,
            "api_key" to BuildConfig.API_KEY,
            "api_secret" to BuildConfig.API_SECRET
        )
    )

    fun uploadImage(imageUri: Uri, context: Context, onResult: (Boolean, String?) -> Unit) {
        try {

            val inputStream = context.contentResolver.openInputStream(imageUri)
            if (inputStream != null) {
                val uploadResult = cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap())
                val imageUrl = uploadResult["secure_url"] as String
                onResult(true, imageUrl)
            } else {
                onResult(false, "Failed to open InputStream from URI")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(false, e.message)
        }
    }
}