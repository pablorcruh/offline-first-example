package ec.com.pablorcruh.onlinefirst.features.task.domain.useCases

import ec.com.pablorcruh.onlinefirst.features.task.domain.repository.TaskRepository
import javax.inject.Inject

class ObservePendingSyncCountUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke() = taskRepository.observePendingSyncCount()
}