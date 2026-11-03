package com.sathiai.assistant.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "mistakes")
data class MistakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val timestamp: Date = Date(),
    val isResolved: Boolean = false
)
