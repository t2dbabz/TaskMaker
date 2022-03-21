package com.example.taskmaker.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmaker.model.Task
import com.example.taskmaker.repository.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val repository: TaskRepository): ViewModel() {

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }



}