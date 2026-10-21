package ec.com.pablorcruh.onlinefirst.features.task.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ec.com.pablorcruh.onlinefirst.features.task.presentation.component.TaskScreenContent

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier
){
    val viewModel = hiltViewModel<TaskViewModel>()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    TaskScreenContent(
        uiState = viewModel.uiState,
        isOnline = isOnline,
        onEvent = viewModel::onEvent
    )
}