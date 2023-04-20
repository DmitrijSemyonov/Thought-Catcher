package com.squalgamesstudio.thoughtcatcher.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.squalgamesstudio.thoughtcatcher.entity.ThoughtEntity

@Database(entities = [ThoughtEntity::class], version = 1, exportSchema = false)
abstract class ThoughtDatabase : RoomDatabase() {
    abstract fun thoughtDao(): ThoughtDao

    companion object{
        @Volatile
        private var INSTANCE: ThoughtDatabase? = null

        fun getDatabase(appContext: Context): ThoughtDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return  tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    appContext, ThoughtDatabase::class.java,
                    ThoughtDatabase::class.simpleName!!
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}