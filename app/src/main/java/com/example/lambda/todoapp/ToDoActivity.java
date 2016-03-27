package com.example.lambda.todoapp;

import android.os.Bundle;

// since we set min sdk as Android Ice Cream Sandwich, not using support libs for earlier android versions.
import android.app.Fragment;
import android.app.Activity;
import android.app.FragmentManager;


/**
 * Created by Lawrence on 3/26/2016.
 *
 * ToDoActivity.java is also a controller.
 *
 * It interacts with ToDoFragment and FrameLayout (fragmentContainer).
 *
 */
public class ToDoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        // Fragments can't put their views on screen, so we use a FragmentManger inside of ToDoActivity.
        // After Honeycomb, the Activity class using Fragment Manager to mange fragments (ToDoFragment)
        // and add their views to the activity's (ToDoActivity) view hierarchy.
        // FragmentManager handles 2 things: list of fragments and a back stack of fragment transactions.
        FragmentManager fragmentManager = getFragmentManager();

        // ask Fragment Manager for fragment with a container view ID of R.id.fragmentContainer
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if( fragment == null ){
            // we need to check for null b/c the call to ToDoActivity.onCreate(...) could be
            // in response to ToDoActivity being RECREATED after being destroyed on rotation
            // or to reclaim memory.
            // Even when Activity is destroyed, its Fragment Manager saves out its list of fragments.
            // when the activity is recreated, the new Fragment Manager retreives the list and recreates
            // the listed fragments to make everything as it was before.

            fragment = new ToDoFragment();
            fragmentManager.beginTransaction()
                    // beginTransaction() creates and returns an instance of FragmentTransaction.
                    // FragmentTransaction class uses a fluent interface
                    // (fluent interface are methods that configure FragmentTransaction to
                    // return a FragmentTransaction instead of void.
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
                    // creates a new fragment transaction, include one add operation, then commit it.
        }


    }
}
