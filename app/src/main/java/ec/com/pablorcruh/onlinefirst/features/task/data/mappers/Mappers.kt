package ec.com.pablorcruh.onlinefirst.features.task.data.mappers

import ec.com.pablorcruh.onlinefirst.coreNetwork.models.CreateTaskRequest
import ec.com.pablorcruh.onlinefirst.coreNetwork.models.TaskDto
import ec.com.pablorcruh.onlinefirst.coreNetwork.models.UpdateTaskRequest
import ec.com.pablorcruh.onlinefirst.coreUtils.SyncStatus
import ec.com.pablorcruh.onlinefirst.features.task.data.local.entities.TaskEntity

fun TaskEntity.toCreateTaskRequest(): CreateTaskRequest{
    return CreateTaskRequest(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted
    )
}

fun TaskEntity.toUpdateTaskeEquest(): UpdateTaskRequest{
    return UpdateTaskRequest(
        title = title,
        description = description,
        isCompleted = isCompleted,
        version = version
    )
}

fun TaskDto.toEntity(): TaskEntity{
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        isDeleted = false,
        createdAt = createdAt,
        updatedAt = updatedAt,
        version = version,
        syncStatus = SyncStatus.SYNC
        )
}