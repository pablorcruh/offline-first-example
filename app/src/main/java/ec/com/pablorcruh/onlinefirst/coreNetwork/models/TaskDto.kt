package ec.com.pablorcruh.onlinefirst.coreNetwork.models

data class TaskDto(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val version: Int,
    val createdAt: Long,
    val updatedAt: Long
)
