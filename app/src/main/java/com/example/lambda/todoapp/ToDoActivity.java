package com.example.lambda.todoapp;

import android.os.Bundle;

// since we set min sdk as Android Ice Cream Sandwich, not using support libs for earlier android versions.
import android.app.Fragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.telephony.SubscriptionManager;

import java.util.UUID;


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

        // ToDoActivity now calls ToDoFragment.newInstance(UUID) when it needs to create a ToDOFragment.
        // note: host activities should be able to know about the specifics of their hostee fragments.
        // so there is a 1-direction coupling.
        UUID todoId = (UUID) getIntent().getSerializableExtra(ToDoFragment.EXTRA_TODO_ID);
        return ToDoFragment.newInstance(todoId);
    }
}
