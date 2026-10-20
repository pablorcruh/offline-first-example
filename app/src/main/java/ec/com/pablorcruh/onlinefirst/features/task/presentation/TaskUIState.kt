package ec.com.pablorcruh.onlinefirst.features.task.presentation

import ec.com.pablorcruh.onlinefirst.features.task.domain.models.Task

data class TaskUIState(
    val isLoading: Boolean = false,
    val error : String = "",
    val isEdit: Boolean = false,
    val editedTask: Task? = null,
    val title: String = "",
    val description: String = "",
    val pendingSyncCount: Int = -1,
    val tasks: List<Task> ?= null
)