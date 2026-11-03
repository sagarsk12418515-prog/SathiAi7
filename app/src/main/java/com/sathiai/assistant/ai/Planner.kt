package com.sathiai.assistant.ai

import com.sathiai.assistant.security.SecureStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class Planner(private val secureStorage: SecureStorage) {

    suspend fun plan(userInput: String): Action = withContext(Dispatchers.IO) {
        // সিকিউর স্টোরেজ থেকে API Key নেওয়া হচ্ছে
        val apiKey = secureStorage.getApiKey("openai") 
            ?: return@withContext Action.Error("OpenAI API Key not found.")

        try {
            val url = URL("https://api.openai.com/v1/chat/completions")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $apiKey")
            conn.doOutput = true

            // AI-কে নির্দেশ দেওয়া হচ্ছে কীভাবে উত্তর দিতে হবে
            val systemPrompt = """
                You are SathiAI, a Bengali-first assistant. 
                Based on the user's input, return ONLY a JSON object with an "action" and "parameters".
                Available actions: "SPEAK", "OPEN_APP".
                Example 1: {"action": "SPEAK", "text": "হ্যালো স্যার, আমি আপনাকে সাহায্য করতে পারি।"}
                Example 2: {"action": "OPEN_APP", "packageName": "com.whatsapp"}
                User Input: $userInput
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("model", "gpt-3.5-turbo")
                put("messages", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", systemPrompt)
                    })
                })
                put("temperature", 0.3)
            }

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(jsonBody.toString())
            writer.flush()
            writer.close()

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(conn.inputStream.reader())
                val response = reader.readText()
                reader.close()

                // রেসপন্স থেকে AI-এর দেওয়া JSON টেক্সটটি বের করা
                val jsonResponse = JSONObject(response)
                val content = jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")

                // AI-এর দেওয়া টেক্সট থেকে শুধু JSON অংশটুকু ফিল্টার করা
                val actionJson = JSONObject(content.substring(content.indexOf("{"), content.lastIndexOf("}") + 1))
                val actionType = actionJson.getString("action")

                when (actionType) {
                    "SPEAK" -> Action.Speak(actionJson.getString("text"))
                    "OPEN_APP" -> Action.OpenApp(actionJson.getString("packageName"))
                    else -> Action.Error("Unknown action from AI")
                }
            } else {
                Action.Error("API Error: ${conn.responseCode}")
            }
        } catch (e: Exception) {
            Action.Error(e.message ?: "Unknown error occurred")
        }
    }
}
