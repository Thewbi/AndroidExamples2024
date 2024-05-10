package com.example.todoapp.ui.add_edit_todo

sealed class AddEditTodoEvent {

    data class onTitleChange(val title: String) : AddEditTodoEvent()

    data class onDescriptionChange(val description: String) : AddEditTodoEvent()

    object onSaveTodoClick : AddEditTodoEvent()

}