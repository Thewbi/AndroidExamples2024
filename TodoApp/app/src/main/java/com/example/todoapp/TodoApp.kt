package com.example.todoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Also add android:name=".TodoApp" to AndroidManifext.xml to the <application> element.
 */
@HiltAndroidApp
class TodoApp: Application()