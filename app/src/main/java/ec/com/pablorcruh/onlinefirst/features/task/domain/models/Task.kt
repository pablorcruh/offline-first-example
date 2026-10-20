package ec.com.pablorcruh.onlinefirst.features.task.domain.models

import ec.com.pablorcruh.onlinefirst.coreUtils.SyncStatus

data class Task(
    val id: String,
    val title: String,
    val description : String,
    val isCompleted: Boolean,
    val syncStatus: SyncStatus,
    val createdAt: Long,
    val updatedAt: Long
)
