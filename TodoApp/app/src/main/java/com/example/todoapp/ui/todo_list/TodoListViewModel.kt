package com.example.todoapp.ui.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.util.Routes
import com.example.todoapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The viewmodel part of the MVVM pattern for the TodoListView.
 */
@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    val todos = repository.getTodos();

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEventFlow = _uiEventChannel.receiveAsFlow()

    private var deletedTodo: Todo? = null

    fun onEvent(event: TodoListEvent) {
        
        when (event) {

            // CREATE
            is TodoListEvent.OnAddTodoClick -> {
                viewModelScope.launch {
                    _uiEventChannel.send(UiEvent.Navigate(Routes.ADD_EDIT_TODO));
                }
            }

            // UPDATE
            is TodoListEvent.OnTodoClick -> {
                viewModelScope.launch {
                    _uiEventChannel.send(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"));
                }
            }

            // UPDATE
            is TodoListEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(event.todo.copy(isDone = event.isDone))
                }

            }

            // DELETE
            is TodoListEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    viewModelScope.launch {
                        UiEvent.ShowSnackbar(
                            message = "Todo deleted",
                            action = "Undo"
                        )
                    }
                }
            }

            // DELETE
            is TodoListEvent.OnUndoDeleteClick -> {
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)
                    }
                }
            }

        }
    }
}