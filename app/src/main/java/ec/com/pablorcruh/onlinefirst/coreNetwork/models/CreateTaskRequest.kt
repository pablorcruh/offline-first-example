package ec.com.pablorcruh.onlinefirst.coreNetwork.models

data class CreateTaskRequest(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)
