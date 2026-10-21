package ec.com.pablorcruh.onlinefirst.features.task.domain.repository

import ec.com.pablorcruh.onlinefirst.features.task.domain.models.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun observeTasks(): Flow<List<Task>>

    suspend fun findById(id: String): Task?

    fun observePendingSyncCount(): Flow<Int>

    suspend fun createTask(
        title: String,
        description: String
    ): Result<Task>

    suspend fun updateTask(
        id: String,
        title: String,
        description: String
    ): Result<Task>

    suspend fun deleteTask(id: String): Result<Unit>

    suspend fun toggleIsCompleted(id: String): Result<Task>
}