package ec.com.pablorcruh.onlinefirst.features.task.data.repository

import ec.com.pablorcruh.onlinefirst.coreUtils.SyncStatus
import ec.com.pablorcruh.onlinefirst.features.task.data.local.TaskDao
import ec.com.pablorcruh.onlinefirst.features.task.data.local.entities.TaskEntity
import ec.com.pablorcruh.onlinefirst.features.task.data.mappers.toDomain
import ec.com.pablorcruh.onlinefirst.features.task.domain.models.Task
import ec.com.pablorcruh.onlinefirst.features.task.domain.repository.TaskRepository
import ec.com.pablorcruh.onlinefirst.features.task.domain.sync.SyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val syncManager: SyncManager
): TaskRepository {
    override fun observeTasks(): Flow<List<Task>> = taskDao.observeAll().map{
        it.map{
            it.toDomain()
        }
    }

    override suspend fun findById(id: String): Task? = taskDao.findById(id)?.toDomain()

    override fun observePendingSyncCount(): Flow<Int> = taskDao.observePendingSyncCount()

    override suspend fun createTask(
        title: String,
        description: String
    ): Result<Task> {
        val taskEntity = TaskEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            isCompleted = false,
            version =1
        )
        taskDao.upsert(taskEntity)
        syncManager.requestSync()
        return Result.success(taskEntity.toDomain())
    }

    override suspend fun updateTask(
        id: String,
        title: String,
        description: String
    ): Result<Task> {
        val existingTask = taskDao.findById(id)?: return Result.failure(Exception("Task not found"))
        val updatedTask = existingTask.copy(
            title = title,
            description = description,
            updatedAt = System.currentTimeMillis(),
            syncStatus = if(existingTask.syncStatus == SyncStatus.PENDING_CREATE) SyncStatus.PENDING_CREATE else SyncStatus.PENDIND_UPDATE
        )

        taskDao.upsert(updatedTask)
        syncManager.requestSync()
        return Result.success(updatedTask.toDomain())

    }

    override suspend fun deleteTask(id: String): Result<Unit> {
        val existingTask = taskDao.findById(id)?: return Result.failure(Exception("Task not found"))
        if(existingTask.syncStatus == SyncStatus.PENDING_DELETE){
            taskDao.deleteById(id)
        }else{
            taskDao.upsert(
                existingTask.copy(
                    isDeleted = true,
                    syncStatus = SyncStatus.PENDING_DELETE
                )
            )
        }
        syncManager.requestSync()
        return Result.success(Unit)
    }

    override suspend fun toggleIsCompleted(id: String): Result<Task> {
        val existingTask = taskDao.findById(id)?: return Result.failure(Exception("Task not found"))
        val updatedTask = existingTask.copy(
            isCompleted = existingTask.isCompleted.not(),
            updatedAt = System.currentTimeMillis(),
            syncStatus = if(existingTask.syncStatus == SyncStatus.PENDING_CREATE) SyncStatus.PENDING_CREATE else SyncStatus.PENDIND_UPDATE
        )
        taskDao.upsert(updatedTask)
        syncManager.requestSync()
        return Result.success(updatedTask.toDomain())
    }

}