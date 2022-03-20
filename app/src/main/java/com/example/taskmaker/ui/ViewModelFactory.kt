package com.example.taskmaker.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskmaker.repository.TaskRepository
import com.example.taskmaker.ui.add.AddTaskViewModel
import com.example.taskmaker.ui.detail.DetailTaskViewModel
import com.example.taskmaker.ui.main.MainViewModel

class ViewModelFactory private constructor(private val taskRepository: TaskRepository) :
    ViewModelProvider.Factory{

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    TaskRepository.getInstance(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(taskRepository) as T
            }
            modelClass.isAssignableFrom(DetailTaskViewModel::class.java) -> {
                DetailTaskViewModel(taskRepository) as T
            }
            modelClass.isAssignableFrom(AddTaskViewModel::class.java) -> {
                AddTaskViewModel(taskRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}