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
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
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
                put("contents", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", org.json.JSONArray().apply {
                            put(JSONObject().apply { put("text", systemPrompt) })
                        })
                    })
                })
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
                return@withContext jsonResponse.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
            } else {
                return@withContext "API Error: ${conn.responseCode}"
            }
        } catch (e: Exception) {
            return@withContext "দুঃখিত, আমি এই মুহূর্তে কথা বলতে পারছি না। এরর: ${e.message}"
        }
    }
}
