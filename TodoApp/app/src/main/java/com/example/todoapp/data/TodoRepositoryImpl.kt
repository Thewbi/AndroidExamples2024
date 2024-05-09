package com.example.todoapp.data

import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(
    private val dao: TodoDao
): TodoRepository {

    override suspend fun insertTodo(todo: Todo) {
        dao.insertTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo)
    }

    override suspend fun getTodayById(id: Int): Todo? {
        return dao.getTodayById(id)
    }

    override fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos()
    }

}