package ec.com.pablorcruh.onlinefirst.coreNetwork.models

data class ChangeLogDto(
    val taskId: String,
    val task: TaskDto,
    val operation: String,
    val cursor: Long
)