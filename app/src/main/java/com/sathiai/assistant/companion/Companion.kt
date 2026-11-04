package com.sathiai.assistant.companion

import com.sathiai.assistant.security.SecureStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class Companion(private val secureStorage: SecureStorage) {

    suspend fun chat(userMessage: String): String = withContext(Dispatchers.IO) {
        val apiKey = secureStorage.getApiKey("openai") 
            ?: return@withContext "দুঃখিত ভাই, আমার API Key সেট করা নেই।"

        try {
            val url = URL("https://openrouter.ai/api/v1/chat/completions")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $apiKey")
            conn.setRequestProperty("HTTP-Referer", "https://github.com")
            conn.setRequestProperty("X-Title", "SathiAI")
            conn.doOutput = true

            val systemPrompt = """
                You are SathiAI, a warm, caring, and respectful Bengali-first companion.
                Your goal is to provide emotional support, motivation, and engage in friendly small talk.
                Always respond in Bengali.
                Be empathetic, encouraging, and kind.
                NEVER generate inappropriate content.
                User says: $userMessage
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                // একদম স্ট্যাবল ফ্রি মডেল
                put("model", "google/gemma-2-9b-it:free")
                put("messages", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "system")
                        put("content", systemPrompt)
                    })
                })
                put("temperature", 0.7)
            }

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(jsonBody.toString())
            writer.flush()
            writer.close()

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(conn.inputStream.reader())
                val response = reader.readText()
                reader.close()

                val jsonResponse = JSONObject(response)
                return@withContext jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
            } else {
                return@withContext "API Error: ${conn.responseCode}"
            }
        } catch (e: Exception) {
            return@withContext "দুঃখিত, আমি এই মুহূর্তে কথা বলতে পারছি না। এরর: ${e.message}"
        }
    }
}
