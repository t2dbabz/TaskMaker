package com.example.taskmaker.ui.main

import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.example.taskmaker.model.Task
import com.example.taskmaker.repository.TaskRepository
import com.example.taskmaker.util.TasksFilter
import kotlinx.coroutines.launch

class MainViewModel(private val repository: TaskRepository) : ViewModel() {

    private val _filter = MutableLiveData<TasksFilter>()

    val tasks : LiveData<PagedList<Task>> = _filter.switchMap { tasksFilter ->
        when(tasksFilter) {
            TasksFilter.ALL_TASKS -> {
                repository.getAllTasks()
            }
            TasksFilter.ACTIVE_TASKS -> {
                repository.getActiveTasks()
            }
            else -> {
                repository.getCompletedTasks()
            }

        }
    }

    init {
        _filter.value = TasksFilter.ALL_TASKS
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task  )
        }
    }
}