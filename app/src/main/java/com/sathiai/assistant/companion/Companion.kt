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

    /**
     * ইউজারের সাথে স্নেহশীল আচরণ এবং মোটিভেশন দেওয়ার ফাংশন
     */
    suspend fun chat(userMessage: String): String = withContext(Dispatchers.IO) {
        val apiKey = secureStorage.getApiKey("openai") 
            ?: return@withContext "দুঃখিত ভাই, আমার API Key সেট করা নেই।"

        try {
            val url = URL("https://api.openai.com/v1/chat/completions")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $apiKey")
            conn.doOutput = true

            // এখানে SathiAI-এর ব্যক্তিত্ব (Persona) সেট করা হচ্ছে
            val systemPrompt = """
                You are SathiAI, a warm, caring, and respectful Bengali-first companion.
                Your goal is to provide emotional support, motivation, and engage in friendly small talk.
                Always respond in Bengali (unless the user specifically speaks in English).
                Be empathetic, encouraging, and kind.
                
                STRICT RULES:
                1. NEVER generate sexual, explicit, or inappropriate content.
                2. NEVER manipulate or falsely flatter the user. Be genuine.
                3. Do not act like a robot; act like a caring friend.
                
                User says: $userMessage
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("model", "gpt-3.5-turbo")
                put("messages", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "system")
                        put("content", systemPrompt)
                    })
                })
                put("temperature", 0.7) // সৃজনশীল এবং আন্তরিক উত্তর দেওয়ার জন্য
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
