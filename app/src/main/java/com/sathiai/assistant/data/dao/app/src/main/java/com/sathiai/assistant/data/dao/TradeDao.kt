package com.sathiai.assistant.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sathiai.assistant.data.entity.TradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Insert
    suspend fun insert(trade: TradeEntity)

    @Query("SELECT * FROM trades ORDER BY timestamp DESC")
    fun getAllTrades(): Flow<List<TradeEntity>>
}
