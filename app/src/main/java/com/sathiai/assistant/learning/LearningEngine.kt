package com.sathiai.assistant.learning

import com.sathiai.assistant.data.dao.KnowledgeDao
import com.sathiai.assistant.data.entity.KnowledgeEntity
import java.util.Date

class LearningEngine(
    private val researchClient: ResearchClient,
    private val knowledgeDao: KnowledgeDao
) {
    /**
     * একটি নির্দিষ্ট টপিকের ওপর রিসার্চ করে তা ডেটাবেসে সেভ করে
     */
    suspend fun learnAboutTopic(topic: String) {
        val researchResult = researchClient.searchGoogle(topic)
        
        // যদি রিসার্চ সফল হয়, তবে ডেটাবেসে সেভ করব
        if (!researchResult.contains("সমস্যা") && !researchResult.contains("পাওয়া যায়নি")) {
            val knowledge = KnowledgeEntity(
                title = topic,
                content = researchResult,
                source = "Google Search",
                timestamp = Date()
            )
            knowledgeDao.insert(knowledge)
        }
    }
}
