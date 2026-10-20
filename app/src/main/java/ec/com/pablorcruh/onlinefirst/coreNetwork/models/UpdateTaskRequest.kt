package ec.com.pablorcruh.onlinefirst.coreNetwork.models

data class UpdateTaskRequest(
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val version: Int
)
