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
public class ToDoActivity extends SingleFragmentActivity{

    // refactored code to be a subclass of SingleFragmentActivity
    @Override
    protected Fragment createFragment() {
        return new ToDoFragment();
    }
}
