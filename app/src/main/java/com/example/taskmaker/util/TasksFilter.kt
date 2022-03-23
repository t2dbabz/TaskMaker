package com.example.taskmaker.util

/**
 * Used with the filter spinner in the tasks list.
 */
enum class TasksFilter {
    /**
     * Do not filter tasks.
     */
    ALL_TASKS,

    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS,

    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS
}