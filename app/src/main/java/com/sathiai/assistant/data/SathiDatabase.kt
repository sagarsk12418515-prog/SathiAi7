package com.sathiai.assistant.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.sathiai.assistant.data.dao.*
import com.sathiai.assistant.data.entity.*
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}

@Database(
    entities = [
        MemoryEntity::class,
        MistakeEntity::class,
        TradeEntity::class,
        KnowledgeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SathiDatabase : RoomDatabase() {
    abstract fun memoryDao(): MemoryDao
    abstract fun mistakeDao(): MistakeDao
    abstract fun tradeDao(): TradeDao
    abstract fun knowledgeDao(): KnowledgeDao
}
