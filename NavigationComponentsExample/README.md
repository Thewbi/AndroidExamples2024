# Introduction

This example makes use of the Jetpack Navigation Component to jump from fragment to fragment based on the user's input into the app.
This example is created in AndroidStudio.

This sample makes use XML for layouts and it does not use Jetpack Compose to programatically define the UI.
The reason is that I do not know how to create a working FragmentContainerView using Jetpack Compose programatically.

*TODO:* How to replace the XML by Jetpack Compose?

This sample is inspired by: https://www.youtube.com/watch?v=IEO2X5OU3MY

## Activity

The idea is to have a single activity (MainActivity).
The MainActivity has to use the @style/Theme.AppCompat as theme in AndroidManifest.xml.

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.NavigationComponentsExample"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.AppCompat">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

The MainActivity has to be derived from AppCompatActivity in order for the app not to throw exceptions on startup.

```
package com.example.navigationcomponentsexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
```

The call to setContentView(R.layout.activity_main) attaches the XML file layout/activity_main.xml to the main activity.

Inside the activity_main.xml, a FragmentCointainerView is placed.

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/my_nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/nav_graph" />

</androidx.constraintlayout.widget.ConstraintLayout>
```



The file activity_main.xml sometimes does not get generated when the project is created initially.
A reason for the absense of the activity_main.xml file might be that your project draws the UI using Jetpack Compose, i.e. generates the UI elements programmatically.

*TODO:* How to create the NavHost viaJetpack Compose?

https://developer.android.com/develop/ui/compose/navigation?hl=de

Should the activity_main.xml not be present, you can create it manually:

* https://www.reddit.com/r/AndroidStudio/comments/12uux4l/not_able_to_find_activity_mainxml/
* https://www.youtube.com/watch?v=YtOMmlujvFE

In order to create the file, create a folder res\layout.
Open the context menu on the folder and select New > Layout Resource File.
Insert the XML into the layout. The XML code is displayed above. The advantage of adding the layout XML file via the IDE instead of
doing it entirely manually is that the IDE will insert a valid skeleton for you and you are sure that the file will work.

## Jetpack Navigation Component

The FragmentContainerView is part of the Jetpack Navigation Component and it's task is to display fragments.
The App itself will create several fragments. Instead of navigating from Activity to Activity, the app
navigates from fragment within the same activity to fragment. The navigation between fragments is defined by the navgraph.

In the FragmentContainerView XML element above, you can see that it references the navigation graph:

```
app:navGraph="@navigation/nav_graph"
```

The navigation graph defines how to jump from navigation to navigation, meaning which transitions are allowed and which are not.
There is a graphical editor for the navigation graph in AndroidStudio.

It is possible to have several activities in the application. Each activity has it's own navigation graph.
In this example, only a single activity with a single navigation graph is used.

*TODO:* How to change to a new activity and activate the new navigation graph?

## nav_graph and fragments

To define the initial fragment, the nav_graph.xml defines all fragments and sets one of the ids into the app:startDestination attribute of the graph.

```
app:startDestination="@id/loginFragment2"
```

## The login fragment

Creating fragments is done by 