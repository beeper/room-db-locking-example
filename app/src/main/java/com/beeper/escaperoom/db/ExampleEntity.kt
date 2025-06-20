package com.beeper.escaperoom.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExampleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val uuid: String,
    val word: String,
    val number: Int
)
