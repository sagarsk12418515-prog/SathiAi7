package com.sathiai.assistant.trading

import com.sathiai.assistant.security.SecureStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class TradingAnalyst(private val secureStorage: SecureStorage) {

    suspend fun analyzeMarket(symbol: String, timeframe: String, currentPrice: Double): String = withContext(Dispatchers.IO) {
        val apiKey = secureStorage.getApiKey("openai") 
            ?: return@withContext "API Key not found. Please set it in Settings."

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
                You are SathiAI, a professional Gold and Global Market Trading Analyst.
                Analyze the market for $symbol on the $timeframe timeframe. Current price is $currentPrice.
                Provide a brief analysis (in Bengali):
                1. Trend (Uptrend/Downtrend/Sideways)
                2. Key Support and Resistance levels
                3. RSI and MACD overview
                4. BUY/SELL Scenario with Confidence (0-100%).
                
                IMPORTANT: You are an analyst. Never say "Execute this trade". 
                End your response exactly with: "This is not investment advice."
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
                put("temperature", 0.4)
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
                val content = jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                
                return@withContext DeepValidator.validateReport(content)
            } else {
                return@withContext "API Error: ${conn.responseCode}"
            }
        } catch (e: Exception) {
            return@withContext "Error during analysis: ${e.message}"
        }
    }
}
