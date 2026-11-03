package com.sathiai.assistant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "knowledge")
data class KnowledgeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val source: String,
    val timestamp: Date = Date()
)
