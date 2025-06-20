package com.beeper.escaperoom.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.QueryCallback
import java.util.concurrent.Executors

@Database(entities = [ExampleEntity::class], version = 1)
abstract class ExampleDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
}

fun database(context: Context): ExampleDatabase {
    return Room.databaseBuilder(context, ExampleDatabase::class.java, "example-db")
        .setQueryCallback(object : QueryCallback {
            override fun onQuery(sql: String, bindArgs: List<Any?>) {
                Log.d("SQL", sql)
            }
        }, Executors.newSingleThreadExecutor())
        .build()
}