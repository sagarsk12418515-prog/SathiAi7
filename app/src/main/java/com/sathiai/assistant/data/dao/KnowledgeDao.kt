package com.sathiai.assistant.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sathiai.assistant.data.entity.KnowledgeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KnowledgeDao {
    @Insert
    suspend fun insert(knowledge: KnowledgeEntity)

    @Query("SELECT * FROM knowledge ORDER BY timestamp DESC")
    fun getAllKnowledge(): Flow<List<KnowledgeEntity>>
}
