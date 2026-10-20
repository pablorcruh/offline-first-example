package ec.com.pablorcruh.onlinefirst.features.task.data.repository

import ec.com.pablorcruh.onlinefirst.features.task.domain.models.Task
import ec.com.pablorcruh.onlinefirst.features.task.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl: TaskRepository {
    override fun observeTasks(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun findById(id: String): Task? {
        TODO("Not yet implemented")
    }

    override fun observePendingSyncCount(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(
        title: String,
        description: String
    ): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(
        id: String,
        title: String,
        description: String
    ): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(id: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleIsCompleted(id: String): Result<Task> {
        TODO("Not yet implemented")
    }
}