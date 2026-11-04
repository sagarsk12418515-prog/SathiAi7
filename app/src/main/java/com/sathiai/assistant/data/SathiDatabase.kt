package com.sathiai.assistant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        @Volatile
        private var INSTANCE: SathiDatabase? = null

        fun getDatabase(context: Context): SathiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SathiDatabase::class.java,
                    "sathi_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
