package ec.com.pablorcruh.onlinefirst.features.task.domain.useCases

import ec.com.pablorcruh.onlinefirst.features.task.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    suspend operator fun invoke(id: String, title: String, description: String) = taskRepository.updateTask(id, title, description)
}