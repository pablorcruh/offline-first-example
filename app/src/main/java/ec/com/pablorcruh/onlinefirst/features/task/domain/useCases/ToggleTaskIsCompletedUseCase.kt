package ec.com.pablorcruh.onlinefirst.features.task.domain.useCases

import ec.com.pablorcruh.onlinefirst.features.task.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleTaskIsCompletedUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: String) = taskRepository.toggleIsCompleted(id)
}