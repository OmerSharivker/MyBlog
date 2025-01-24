package com.example.myblog.data.api

import com.google.ai.client.generativeai.GenerativeModel
import com.example.myblog.BuildConfig

class GeminiService {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun generateFunnySentence(): String {
        return try {
            val response = generativeModel.generateContent("Write a funny 40-word sentence in English.")
            response.text ?: "No text generated"
        } catch (e: Exception) {
            e.printStackTrace()
            "Error generating text: ${e.message}"
        }
    }
}