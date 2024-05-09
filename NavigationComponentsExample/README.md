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

Inside the activity_main.xml, a FragmentContainerView is placed.

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

Creating fragments is done via the IDE.

Open the context menu and select New > Fragment > ...

From the list of available fragments, the login fragment is created by selecting "Login Fragment".
The Login Fragment uses the MVVM pattern and the IDE will generate the entire class structure that is required to run the Login functionality.

The UI elements are created in res\layout\fragment_login.xml

The user types in a username and a password into the input fields, then taps the login button.

MVVM stands for [Model] [View] [ViewModel].
The ViewModel sits between the model and the view and takes on the role of a mediator.
The View does not know what is performed with it's user input and it does not know how the data is created that is has to display.
The Model on the other hand has no idea how the data is displayed in the UI. Instead it just returns DataObjects.

The ViewModel in between receives the user input and knows how to turn this user input into data using application logic.
The application logic is implemented inside the view model.

### Interaction between the View and the View Model

Lets look at how the view is interfacing with the view model and how the login button works.

Inside LoginFragment.kt, there is a binding, which gives the class access to the UI elements defined in the XML layout.

```
val loginButton = binding.login

...

loginButton.setOnClickListener {
	loadingProgressBar.visibility = View.VISIBLE
	loginViewModel.login(
		usernameEditText.text.toString(),
		passwordEditText.text.toString()
	)
}
```

This onClick listener will read the text from the username and password fields and pass those values into a call to
loginViewModel.login().

This is how the view talks to the view model in the MVVM pattern. The view does not care what happens with the data.

The login() funtion in the loginViewModel is:

```
fun login(username: String, password: String) {
	// can be launched in a separate asynchronous job
	val result = loginRepository.login(username, password)

	if (result is Result.Success) {
		_loginResult.value =
			LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
	} else {
		_loginResult.value = LoginResult(error = R.string.login_failed)
	}
}
```

The view model talks to the repository in oder to find out if the login suceeded or failed.

```
val result = loginRepository.login(username, password)
```

The repsitory will answer with a result. The view model now modifies the MutableLiveData<LoginResult>() object.
The view (= LoginFragment.kt) is registered as an observer to this mutable and will react to the changes made to the MutableLiveData<LoginResult>() object.
See LoginFragment.kt

```
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

	...
	
	loginViewModel.loginResult.observe(viewLifecycleOwner,
		Observer { loginResult ->
			loginResult ?: return@Observer
			loadingProgressBar.visibility = View.GONE
			loginResult.error?.let {
				showLoginFailed(it)
			}
			loginResult.success?.let {
				updateUiWithUser(it)
			}
		})
		
	...
	
}
```

This is how the UI reacts to changes inside the view model. The view does not know about the application logic that changes the data.
It just displays the updated data.

Instead of updateUiWithUser(it) the application should navigate to the next fragment.
If the login fails, the application should stay on the login fragment.

### Interaction between the ViewModel and the Model

The model consists of the repository which in turn talks to the DataSource.
The repository LoginRepository.kt uses the data source (LoginDataSource.kt) to check if the login can be completed using the
username and the password.

```
fun login(username: String, password: String): Result<LoggedInUser> {
	// handle login
	val result = dataSource.login(username, password)

	if (result is Result.Success) {
		setLoggedInUser(result.data)
	}

	return result
}
```

The repository's result is retrieved by the viewmodel.
The viewmodel will update it's MutableLiveData with said result which in turn triggers the observable that is part of the view and which
is registered at the mutable. This is how the result information makes it's way back into the UI without the UI knowing details of how
this data was retrieved.

# Patientlist Fragment

Navigating to another fragment after successfull login is done using a navigation controller.
First define a new fragment to navigate to.

ObjectKind: PatientList
Fragment class name: PatientListFragment
ColumnCount: 1 column
Object content layout file: patient_list_fragment_item
List layout file name: patient_list_fragment_item_list
Adapter class name: PatientListRecyclerViewAdapter
Source Language: Kotlin
Target Source Set: main

Open the nav_graph editor on your nav_graph XML file and insert the new fragment.
Draw a transition between the login fragment and the new fragment.

To trigger the navigation, the login fragment is updated.
First, add the navigation controller member variable into the login fragment.

```
private lateinit var navController: NavController
```

the lateinit keyword allows the variable to be left with a null value.
This means, the code does not instantiate the nav controller.
Instead the navcontroller will be retrieved from the system inside onViewCreated()

Now inside onViewCreate() of the fragment, the nav controller is assigned.

```
navController = Navigation.findNavController(view)
```
