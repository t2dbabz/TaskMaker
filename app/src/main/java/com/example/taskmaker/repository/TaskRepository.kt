package com.example.taskmaker.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.taskmaker.database.TaskDao
import com.example.taskmaker.database.TaskDatabase
import com.example.taskmaker.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    companion object {
        const val PAGE_SIZE = 30
        const val PLACEHOLDERS = true

        @Volatile
        private var instance: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = TaskDatabase.getInstance(context)
                    instance = TaskRepository(database.taskDao())
                }
                return instance as TaskRepository
            }

        }
    }

    fun getAllTasks() : LiveData<PagingData<Task>> {
        val config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = PLACEHOLDERS)
        val pagingSource = taskDao.getAllTask()
        return Pager(config) {
            pagingSource
        }.liveData
    }

    fun getCompletedTasks() : LiveData<PagingData<Task>> {
        val config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = PLACEHOLDERS)
        val pagingSource = taskDao.getCompletedTasks()
        return Pager(config) {
            pagingSource
        }.liveData
    }

    fun getActiveTasks() : LiveData<PagingData<Task>> {
        val config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = PLACEHOLDERS)
        val pagingSource = taskDao.getActiveTasks()
        return Pager(config) {
            pagingSource
        }.liveData
    }

    fun getTaskById(taskId: Int): LiveData<Task> {
        return taskDao.getTaskById(taskId)
    }


    suspend fun insertTask(newTask: Task) {
        return taskDao.insertTask(newTask)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }


}