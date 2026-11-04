package com.sathiai.assistant.learning

import com.sathiai.assistant.security.SecureStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ResearchClient(private val secureStorage: SecureStorage) {

    /**
     * Google Custom Search API ব্যবহার করে ইন্টারনেট থেকে রিসার্চ করা
     */
    suspend fun searchGoogle(query: String): String = withContext(Dispatchers.IO) {
        val apiKey = secureStorage.getApiKey("google_search")
        val cx = secureStorage.getApiKey("google_cx")
        
        if (apiKey == null || cx == null) {
            return@withContext "Google Search API Key বা CX পাওয়া যায়নি।"
        }

        try {
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val url = URL("https://www.googleapis.com/customsearch/v1?key=$apiKey&cx=$cx&q=$encodedQuery")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(conn.inputStream.reader())
                val response = reader.readText()
                reader.close()

                val jsonResponse = JSONObject(response)
                val items = jsonResponse.optJSONArray("items")
                
                if (items != null && items.length() > 0) {
                    val firstResult = items.getJSONObject(0)
                    val title = firstResult.getString("title")
                    val snippet = firstResult.getString("snippet")
                    return@withContext "শিরোনাম: $title\nসারসংক্ষেপ: $snippet"
                }
            }
            return@withContext "কোনো ফলাফল পাওয়া যায়নি।"
        } catch (e: Exception) {
            return@withContext "রিসার্চ করতে সমস্যা হয়েছে: ${e.message}"
        }
    }
}
