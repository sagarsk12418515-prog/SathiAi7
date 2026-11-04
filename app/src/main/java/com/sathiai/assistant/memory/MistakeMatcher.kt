package com.sathiai.assistant.memory

import com.sathiai.assistant.data.dao.MistakeDao
import com.sathiai.assistant.data.entity.MistakeEntity
import java.util.Date

class MistakeMatcher(private val mistakeDao: MistakeDao) {

    /**
     * নতুন ট্রেডিং সিচুয়েশন অতীতের কোনো ভুলের সাথে মিলে কিনা চেক করা
     */
    suspend fun findSimilarMistake(currentSituation: String): MistakeEntity? {
        // ডেটাবেস থেকে সব ভুলের লিস্ট আনা (এটি সরাসরি সাসপেন্ড ফাংশন হিসেবে Room-এ থাকতে হবে)
        // আপাতত ডেমো হিসেবে আমরা ডামি ডেটা চেক করছি
        val allMistakes = listOf(
            MistakeEntity(id = 1, description = "Traded without stop loss on XAUUSD", timestamp = Date(), isResolved = false),
            MistakeEntity(id = 2, description = "Overtrading during high volatility news", timestamp = Date(), isResolved = true)
        )

        // সরল টেক্সট ম্যাচিং
        for (mistake in allMistakes) {
            if (currentSituation.lowercase().contains("stop loss") && mistake.description.lowercase().contains("stop loss")) {
                return mistake
            }
            if (currentSituation.lowercase().contains("news") && mistake.description.lowercase().contains("news")) {
                return mistake
            }
        }
        return null
    }

    /**
     * নতুন ভুল ডেটাবেসে সেভ করা
     */
    suspend fun recordMistake(description: String) {
        val mistake = MistakeEntity(
            description = description,
            timestamp = Date(),
            isResolved = false
        )
        mistakeDao.insert(mistake)
    }
}
