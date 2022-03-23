package com.example.taskmaker.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.example.taskmaker.model.Task


@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTask(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1")
    fun getCompletedTasks(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0")
    fun getActiveTasks(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int) : LiveData<Task>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 LIMIT 6")
    fun getNearestActiveTasks(): List<Task>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

}