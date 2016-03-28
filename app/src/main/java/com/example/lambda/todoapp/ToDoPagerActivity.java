package com.example.lambda.todoapp;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lawrence on 3/28/2016.
 *
 * This Pager will allow swiping through the different ToDoFragments left and right.
 *
 * This is part of the controller layer. It also sits between the ToDoListFragment (controller)
 * and the ToDoFragment (controller).
 *
 */
public class ToDoPagerActivity extends FragmentActivity{

    private ViewPager mViewPager;
    private ArrayList<ToDo> mToDos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // since the layout will be simple, we'll define layout in code rather than xml.
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);       // we still have to define the id in ids.xml
        setContentView(mViewPager);

        mToDos = ToDoList.get(this).getToDos();     // initialize member with data from singleton

        FragmentManager fm = getSupportFragmentManager();
        // set adapter to be an unnamed instance of FragmentStatePagerAdapter.
        // this manages the conversation with ViewPager
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                ToDo t = mToDos.get(position);
                return ToDoFragment.newInstance(t.getId());
            }

            @Override
            public int getCount() {
                return mToDos.size();
            }
        });

        // by default, the Pager starts on the 1st item (even if we click on the 10th item).
        // so we have to add some extra logic to get Pager to start on the item we clicked.
        UUID todoId = (UUID) getIntent().getSerializableExtra(ToDoFragment.EXTRA_TODO_ID);
        for( int i=0; i < mToDos.size(); ++i ){
            if( mToDos.get(i).getId().equals(todoId) ){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        // we could set the action bar to match the title of our todo,
        // using mViewPager.setOnPageChangeListener() but it is deprecated and I can't get the action bar
        // to show on the activity. so skipping it.

        // FragmentStatePagerAdapter vs. FragmentPagerAdapter
        // The only diff between them is that StatePager destroys (saves fragment's Bundle from onSavedInstanceState(Bundle))
        // Fragments that are no longer used. When users navigate back, it restores using that instance state.
        // Pager doesn't do that. it just calls detach(Fragment) on the transaction instead of remove(Fragment).



    } // end onCreate


}
