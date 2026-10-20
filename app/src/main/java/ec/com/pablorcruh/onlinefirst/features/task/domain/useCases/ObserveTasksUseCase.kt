package ec.com.pablorcruh.onlinefirst.features.task.domain.useCases

import ec.com.pablorcruh.onlinefirst.features.task.domain.repository.TaskRepository
import javax.inject.Inject

class ObserveTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke() = taskRepository.observeTasks()
}