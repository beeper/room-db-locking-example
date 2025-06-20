package com.beeper.escaperoom.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExampleDao {
    @Query("SELECT * FROM ExampleEntity limit 1")
    fun getExampleFlow(): Flow<ExampleEntity?>

    @Query("SELECT * FROM ExampleEntity limit 1")
    suspend fun getExample(): ExampleEntity?
}