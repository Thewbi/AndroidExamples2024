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
    todoListViewModel: TodoListViewModel = hiltViewModel()
) {

    val todos = todoListViewModel.todos.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }

    //val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {

        // subscribe to the uiEventFlow of the view model.
        // This flow will emit events for navigating into todos
        // and events for undeleting todos
        todoListViewModel.uiEventFlow.collect { event ->

            when (event) {

                // undo delete
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        todoListViewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }

                // navigate to the AddEditTodoScreen
                //
                // The navigation event is send by ???
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

        // button to add a new todo
        floatingActionButton = {
            FloatingActionButton(onClick = {
                todoListViewModel.onEvent(TodoListEvent.OnAddTodoClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },

        // insert a list view of all items
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                //modifier = Modifier.fillMaxSize()
            ) {

                items(todos.value) { todo ->

                    TodoItem(


                        todo = todo,

                        // here, the onEvent() function of the todoListViewModel is passed
                        // into the TodoItem, so that the TodoItem can call the method and
                        // send events to the todoListViewModel.
                        onEvent = todoListViewModel::onEvent
                    )

                }

            }
        }
    )
}
