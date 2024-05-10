package com.example.todoapp.ui.add_edit_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEventChannel = Channel<UiEvent>()
    val uiEventFlow = _uiEventChannel.receiveAsFlow()

    init {
        val todoId = savedStateHandle.get<Int>("todoId")!!

        // the todo id -1 is a dummy value which is used for newly created todos
        if (todoId != -1) {
            viewModelScope.launch {
                repository.getTodoById(todoId)?.let { todo ->
                    title = todo.title
                    description = todo.description ?: ""
                    this@AddEditTodoViewModel.todo = todo
                }

            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.onTitleChange -> {
                title = event.title
            }

            is AddEditTodoEvent.onDescriptionChange -> {
                description = event.description
            }

            is AddEditTodoEvent.onSaveTodoClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        viewModelScope.launch {
                            _uiEventChannel.send(
                                UiEvent.ShowSnackbar(
                                    message = "The title can't be empty"
                                )
                            )
                            return@launch
                        }
                    }
                    repository.insertTodo(
                        Todo(
                            title = title,
                            description = description,
                            isDone = todo?.isDone ?: false,
                            id = todo?.id
                        )
                    )
                    viewModelScope.launch {
                        _uiEventChannel.send(UiEvent.PopBackStack)
                    }
                }
            }
        }
    }
}