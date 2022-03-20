package com.example.taskmaker.model

data class Task(
    val id: Int,
    val taskTitle: String,
    val isCompleted: Boolean = false,
    val isPriority: Boolean = false,
    val dueDate: Long
)
