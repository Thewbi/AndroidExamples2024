package com.example.todoapp.di

import android.app.Application
import androidx.room.Room
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This is where the objects are created that are then injected into the
 * application to provide the services, repositories, ...
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TodoDatabase {
        return Room.databaseBuilder(
            app, TodoDatabase::class.java,
            "todo_db"
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
        return TodoRepositoryImpl(db.dao);
    }

}