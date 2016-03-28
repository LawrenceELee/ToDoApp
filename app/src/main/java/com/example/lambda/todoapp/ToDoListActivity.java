package com.example.lambda.todoapp;

import android.support.v4.app.Fragment;

/**
 * Created by Lawrence on 3/27/2016.
 *
 *
 */
public class ToDoListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ToDoListFragment();
    }
}
