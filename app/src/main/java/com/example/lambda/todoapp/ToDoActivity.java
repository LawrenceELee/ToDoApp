package com.example.lambda.todoapp;

import android.support.v4.app.Fragment;

// since we set min sdk as Android Ice Cream Sandwich, not using support libs for earlier android versions.

import java.util.UUID;


/**
 * Created by Lawrence on 3/26/2016.
 *
 * ToDoActivity.java is also a controller.
 *
 * It interacts with ToDoFragment and FrameLayout (fragmentContainer).
 *
 * This java file is extraneous since we are using a ViewPager (ToDoPagerActivity).
 *
 */
public class ToDoActivity extends SingleFragmentActivity{

    // refactored code to be a subclass of SingleFragmentActivity
    @Override
    protected Fragment createFragment() {

        // ToDoActivity now calls ToDoFragment.newInstance(UUID) when it needs to create a ToDOFragment.
        // note: host activities should be able to know about the specifics of their hostee fragments.
        // so there is a 1-direction coupling.
        UUID todoId = (UUID) getIntent().getSerializableExtra(ToDoFragment.EXTRA_TODO_ID);
        return ToDoFragment.newInstance(todoId);
    }
}
