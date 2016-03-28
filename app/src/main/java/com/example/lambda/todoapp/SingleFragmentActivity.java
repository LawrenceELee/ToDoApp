package com.example.lambda.todoapp;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Lawrence on 3/27/2016.
 *
 * Since the code to create a Fragment is non-specific to a particular fragment (i.e. it is generic)
 * we'll create an abstract class SingleFragmentActivity that future fragment activities will use.
 *
 */
public abstract class SingleFragmentActivity extends FragmentActivity {

    // this is the only diff between this and ToDoActivity.java
    // it is used to instantiate the fragment.
    // subclass of SingleFragmentActivity will implement this method to return
    // an instance of the fragment that the activity is hosting.
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        // Fragments can't put their views on screen, so we use a FragmentManger inside of ToDoActivity.
        // After Honeycomb, the Activity class using Fragment Manager to mange fragments (ToDoFragment)
        // and add their views to the activity's (ToDoActivity) view hierarchy.
        // FragmentManager handles 2 things: list of fragments and a back stack of fragment transactions.
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        // ask Fragment Manager for fragment with a container view ID of R.id.fragmentContainer
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if( fragment == null ){
            // we need to check for null b/c the call to ToDoActivity.onCreate(...) could be
            // in response to ToDoActivity being RECREATED after being destroyed on rotation
            // or to reclaim memory.
            // Even when Activity is destroyed, its Fragment Manager saves out its list of fragments.
            // when the activity is recreated, the new Fragment Manager retreives the list and recreates
            // the listed fragments to make everything as it was before.

            fragment = createFragment();    // this is also different, we wrap createFragment around each Fragment to create depending on type (ToDoListFragment or ToDoFragment).

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
