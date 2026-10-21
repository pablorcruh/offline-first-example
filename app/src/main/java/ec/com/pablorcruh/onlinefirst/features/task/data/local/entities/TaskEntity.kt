package ec.com.pablorcruh.onlinefirst.features.task.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ec.com.pablorcruh.onlinefirst.coreUtils.SyncStatus

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val isDeleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val version: Int,
    val syncStatus: SyncStatus = SyncStatus.SYNC
)
