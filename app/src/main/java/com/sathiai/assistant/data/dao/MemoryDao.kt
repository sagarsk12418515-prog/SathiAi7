package com.sathiai.assistant.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sathiai.assistant.data.entity.MemoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoryDao {
    @Insert
    suspend fun insert(memory: MemoryEntity)

    @Query("SELECT * FROM memories ORDER BY timestamp DESC")
    fun getAllMemories(): Flow<List<MemoryEntity>>
}
