package com.sathiai.assistant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "trades")
data class TradeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val symbol: String,
    val action: String, 
    val entryPrice: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val timestamp: Date = Date()
)
