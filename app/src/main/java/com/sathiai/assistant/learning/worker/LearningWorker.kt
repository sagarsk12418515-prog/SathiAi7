package com.sathiai.assistant.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sathiai.assistant.data.SathiDatabase
import com.sathiai.assistant.learning.LearningEngine
import com.sathiai.assistant.learning.ResearchClient
import com.sathiai.assistant.security.SecureStorage

class LearningWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // ডাটাবেস এবং সিকিউর স্টোরেজ তৈরি করা
            val db = SathiDatabase.getDatabase(applicationContext)
            val secureStorage = SecureStorage(applicationContext)
            val researchClient = ResearchClient(secureStorage)
            val learningEngine = LearningEngine(researchClient, db.knowledgeDao())

            // এখানে ইউজারের আগ্রহের টপিকগুলো দিতে হবে (পরবর্তীতে ডেটাবেস থেকে আসবে)
            val topicsToLearn = listOf("Gold Trading Analysis", "XAUUSD technical analysis", "Global Market News")
            
            for (topic in topicsToLearn) {
                learningEngine.learnAboutTopic(topic)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
