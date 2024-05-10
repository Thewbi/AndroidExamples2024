package com.example.todoapp.ui.todo_list;

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.util.UiEvent

@Composable
fun TodoListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    //val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        viewModel.uiEventFlow.collect { event ->
            when (event) {

                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }

                is UiEvent.Navigate -> {
                    onNavigate(event)
                }

                else -> Unit
            }
        }
    }

    // https://developer.android.com/develop/ui/compose/designsystems/material2-material3?hl=de#m3_6
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TodoListEvent.OnAddTodoClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                //modifier = Modifier.fillMaxSize()
            ) {
                items(todos.value) { todo ->
                    TodoItem(
                        todo = todo,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    )
}
