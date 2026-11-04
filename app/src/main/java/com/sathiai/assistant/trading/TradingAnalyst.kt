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
            ?: return@withContext "OpenAI API Key not found."

        try {
            val url = URL("https://api.openai.com/v1/chat/completions")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $apiKey")
            conn.doOutput = true

            val systemPrompt = """
                You are SathiAI, a professional Gold and Global Market Trading Analyst.
                Analyze the market for $symbol on the $timeframe timeframe. Current price is $currentPrice.
                Provide a detailed analysis using the following structure:
                1. Market Structure & Trend (Higher Highs/Lows)
                2. Key Levels: Support, Resistance, Supply/Demand, Order Blocks
                3. Indicators: RSI, MACD, Bollinger Bands, VWAP, ATR, Ichimoku
                4. Fibonacci Levels
                5. Candlestick Patterns
                6. News & Global Market Impact (DXY, US10Y)
                7. Consensus: Provide a BUY or SELL analytical scenario with Confidence (0-100%).
                8. Risk Management: Suggest a possible Stop Loss and Take Profit levels.
                IMPORTANT: You are an analyst, NOT a broker. Never say "Execute this trade". 
                Provide the analysis in Bengali language.
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("model", "gpt-4")
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
