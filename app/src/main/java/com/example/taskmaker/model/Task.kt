package com.example.taskmaker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "taskTitle")
    val taskTitle: String,

    @ColumnInfo(name = "isCompleted")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "isPriority")
    val isPriority: Boolean = false,

    @ColumnInfo(name = "dueDateMillis")
    val dueDateMillis: Long
)
