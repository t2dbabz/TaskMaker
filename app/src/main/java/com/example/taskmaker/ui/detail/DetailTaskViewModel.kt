package com.example.taskmaker.ui.detail

import androidx.lifecycle.*
import com.example.taskmaker.repository.TaskRepository
import kotlinx.coroutines.launch


class DetailTaskViewModel(private val repository: TaskRepository): ViewModel() {

    private val _taskId = MutableLiveData<Int>()

    val task = _taskId.switchMap { taskId ->
        repository.getTaskById(taskId)
    }

    fun setTaskId(taskId: Int) {
        if (_taskId.value == taskId){
            return
        }

        _taskId.value = taskId
    }

    fun deleteTask() {
        viewModelScope.launch {
            task.value?.let { repository.deleteTask(it) }
        }
    }
}