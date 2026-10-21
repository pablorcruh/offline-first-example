package ec.com.pablorcruh.onlinefirst.features.task.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ec.com.pablorcruh.onlinefirst.features.task.presentation.TaskEvent
import ec.com.pablorcruh.onlinefirst.features.task.presentation.TaskUIState
import kotlinx.coroutines.launch

@Composable
fun TaskScreenContent(
    modifier: Modifier = Modifier,
    uiState: TaskUIState,
    isOnline: Boolean,
    onEvent: (TaskEvent) -> Unit
){
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        animationSpec = tween (1000)
    )

    val scope = rememberCoroutineScope()

    LaunchedEffect(sheetState.currentValue) {
        if(sheetState.currentValue == ModalBottomSheetValue.Hidden){
            onEvent(TaskEvent.ClearEditInformation)
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter task details",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(value = uiState.title, onValueChange = {
                    onEvent(TaskEvent.OnTitleChange(it))
                }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(value = uiState.description, onValueChange = {
                    onEvent(TaskEvent.OnDescriptionChange(it))
                }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    if(uiState.isEdit){
                        onEvent(TaskEvent.CreateTask)
                    }else{
                        onEvent(TaskEvent.UpdateTask)
                    }
                }) {
                    Text(
                        if(uiState.isEdit) "Update" else "Create"
                    )
                }
            }
        },
        sheetShape = RoundedCornerShape(topStart =12.dp, topEnd = 12.dp)
    ) {

        Scaffold(modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {Text("Your Tasks")})
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    scope.launch{
                        sheetState.show()
                    }
                }){
                    Text("+")
                }
            }) {innerPadding ->

            Column(
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {
                AnimatedVisibility(isOnline) {
                    if(isOnline.not())Text("No Internet Connection")
                }
                when{
                    uiState.isLoading ->{
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator()
                        }
                    }
                    uiState.error.isNotEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ){
                            Text(uiState.error)
                        }
                    }
                    else -> {
                        if(uiState.tasks != null){
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(
                                    count = uiState.tasks.size,
                                    key = { index -> uiState.tasks[index].id },  // map index -> stable id
                                    contentType = { 0 }
                                ){
                                        index ->
                                    val item = uiState.tasks[index]
                                    Row(
                                        modifier = Modifier
                                            .combinedClickable(
                                                onLongClick = {
                                                    onEvent(TaskEvent.DeleteTask(item.id))
                                                },
                                                onClick = {
                                                    onEvent(TaskEvent.OnEdit(item.id))
                                                    scope.launch{
                                                        sheetState.show()
                                                    }
                                                }
                                            )
                                            .padding(12.dp).fillMaxWidth()
                                    ) {
                                        Checkbox(
                                            checked = item.isCompleted,
                                            onCheckedChange = {
                                                onEvent(TaskEvent.Toogle(item.id))
                                            }
                                        )
                                        Spacer(
                                            modifier = Modifier.width(12.dp)
                                        )
                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(item.title, style = MaterialTheme.typography.bodySmall)
                                            Spacer(Modifier.height(8.dp))
                                            Text(item.description)
                                        }
                                    }
                                }
                            }
                        }else{
                            Box(
                                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                            ){
                                Text("Please Add Task")
                            }
                        }
                    }


                }
            }
        }
    }
}