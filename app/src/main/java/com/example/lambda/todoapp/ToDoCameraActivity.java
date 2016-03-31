package com.example.lambda.todoapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Lawrence on 3/30/2016.
 *
 * This is part of the controller layer.
 *
 * It will be called by the ToDoFragment.
 *
 * In turn this will host/call the ToDoCameraFragment.
 *
 */
public class ToDoCameraActivity extends SingleFragmentActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // you can't do this in the Fragment, you can only do this in the parent host activity.
        // hide the action bar when camera is on
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide the status bar and other OS-level chrome
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new ToDoCameraFragment();
    }
}
