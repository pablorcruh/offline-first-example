package ec.com.pablorcruh.onlinefirst.features.task.presentation

sealed interface TaskEvent {

    data object CreateTask: TaskEvent

    data object UpdateTask: TaskEvent

    data class DeleteTask(val id: String): TaskEvent

    data class Toogle(val id: String): TaskEvent

    data class OnTitleChange(val title: String): TaskEvent

    data class OnDescriptionChange(val description: String): TaskEvent

    data class OnEdit(val id:String): TaskEvent

    data object ClearEditInformation: TaskEvent
}