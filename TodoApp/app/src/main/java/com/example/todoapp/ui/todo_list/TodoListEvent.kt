package com.example.todoapp.ui.todo_list

import com.example.todoapp.data.Todo

sealed class TodoListEvent {

    object OnAddTodoClick : TodoListEvent()

    data class OnTodoClick(val todo: Todo) : TodoListEvent()

    data class OnDoneChange(val todo: Todo, val isDone: Boolean) : TodoListEvent()

    data class OnDeleteTodoClick(val todo: Todo) : TodoListEvent()

    object OnUndoDeleteClick : TodoListEvent()
    
}