package ec.com.pablorcruh.onlinefirst.features.task.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.com.pablorcruh.onlinefirst.coreConnectivity.ConnectivityMonitor
import ec.com.pablorcruh.onlinefirst.features.task.domain.useCases.CreateTaskUseCase
import ec.com.pablorcruh.onlinefirst.features.task.domain.useCases.DeleteTaskUseCase
import ec.com.pablorcruh.onlinefirst.features.task.domain.useCases.FindTaskByIdUseCase
import ec.com.pablorcruh.onlinefirst.features.task.domain.useCases.ObservePendingSyncCountUseCase
import ec.com.pablorcruh.onlinefirst.features.task.domain.useCases.ObserveTasksUseCase
import ec.com.pablorcruh.onlinefirst.features.task.domain.useCases.ToggleTaskIsCompletedUseCase
import ec.com.pablorcruh.onlinefirst.features.task.domain.useCases.UpdateTaskUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val findTaskByIdUseCase: FindTaskByIdUseCase,
    private val toggleTaskIsCompletedUseCase: ToggleTaskIsCompletedUseCase,
    private val observeTasksUseCase: ObserveTasksUseCase,
    private val observePendingSyncCountUseCase: ObservePendingSyncCountUseCase,
    private val connectivityMonitor: ConnectivityMonitor
): ViewModel() {

    var uiState by mutableStateOf(TaskUIState())
        private set

    val isOnline = connectivityMonitor.isOnlineFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    init{
        observeTasksUseCase()
            .onEach { tasks -> uiState = uiState.copy(
                tasks = tasks
            ) }
            .launchIn(viewModelScope)

        observePendingSyncCountUseCase()
            .onEach { pendingSyncCount ->
                uiState = uiState.copy(
                    pendingSyncCount = pendingSyncCount
                )
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: TaskEvent){
        when(event){
            TaskEvent.CreateTask -> createTask()
            TaskEvent.UpdateTask -> updateTask()
            is TaskEvent.OnTitleChange ->{
                uiState = uiState.copy(
                    title = event.title
                )
            }
            is TaskEvent.OnDescriptionChange -> {
                uiState = uiState.copy(
                    description = event.description
                )
            }
            is TaskEvent.Toogle -> toogleIsCompleted(event.id)
            is TaskEvent.DeleteTask -> deleteTask(event.id)
            is TaskEvent.OnEdit -> onEdit(event.id)
        }
    }

    private fun createTask() = viewModelScope.launch {
        uiState = uiState.copy(error = "")
        createTaskUseCase(
            title = uiState.title,
            description = uiState.description
        ).onSuccess { data ->
            uiState = uiState.copy(
                title = "",
                description = ""
            )
        }.onFailure { error ->
            uiState = uiState.copy(
                error = error.message.toString()
            )
        }
    }

    private fun updateTask() = viewModelScope.launch {
        uiState = uiState.copy(error = "")
        if(uiState.isEdit && uiState.editedTask != null){
            updateTaskUseCase(
                id = uiState.editedTask!!.id,
                title = uiState.title,
                description = uiState.description
            ).onSuccess { data ->
                uiState = uiState.copy(
                    isEdit = false,
                    editedTask = null,
                    title = "",
                    description = ""
                )
            }.onFailure { error ->
                uiState = uiState.copy(error = error.message.toString())
            }
        }
    }

    private fun deleteTask(id: String) = viewModelScope.launch{
        deleteTaskUseCase(id)
    }

    fun toogleIsCompleted(id: String) = viewModelScope.launch{
        toggleTaskIsCompletedUseCase(id)
    }


    private fun onEdit(id: String) = viewModelScope.launch {
        val task = findTaskByIdUseCase(id)
        task?.let{
            uiState = uiState.copy(
                isEdit = true,
                editedTask = it,
                title = it.title,
                description = it.description
            )
        }
    }

}