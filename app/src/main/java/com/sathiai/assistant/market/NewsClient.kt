package com.sathiai.assistant.market

import com.sathiai.assistant.security.SecureStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class MarketNews(
    val title: String,
    val source: String,
    val timestamp: String
)

class NewsClient(private val secureStorage: SecureStorage) {

    /**
     * গ্লোবাল মার্কেট নিউজ আনা এবং বাংলায় অনুবাদ করা
     */
    suspend fun fetchAndTranslateNews(): List<MarketNews> = withContext(Dispatchers.IO) {
        // আপাতত আমরা ডেমো নিউজ দেখাচ্ছি, পরবর্তীতে এখানে আসল নিউজ API বসবে
        val demoNews = listOf(
            MarketNews("Fed signals potential rate cuts later this year.", "Reuters", "10 mins ago"),
            MarketNews("Gold surges as US Dollar weakens amid global tensions.", "Bloomberg", "1 hour ago"),
            MarketNews("Crude oil prices steady ahead of OPEC+ meeting.", "CNBC", "2 hours ago")
        )

        // ভবিষ্যতে এখানে OpenAI API দিয়ে নিউজগুলোকে বাংলায় অনুবাদ করা হবে
        // আপাতত বাংলা ভার্সন ডেমো হিসেবে দেখাচ্ছি
        return@withContext demoNews.map { news ->
            news.copy(
                title = when {
                    news.title.contains("Gold") -> "সোনার দাম বেড়েছে কারণ ডলার দুর্বল হয়েছে।"
                    news.title.contains("Fed") -> "মার্কিন ফেডারেল রিজার্ভ সুদের হার কমানোর ইঙ্গিত দিয়েছে।"
                    news.title.contains("Crude") -> "ওপেক প্লাসের বৈঠকের আগে অপরিশোধিত তেলের দাম স্থিতিশীল আছে।"
                    else -> news.title
                }
            )
        }
    }
}
