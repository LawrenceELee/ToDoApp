package com.example.lambda.todoapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lawrence on 3/27/2016.
 *
 * This is part of the model layer.
 *
 * We'll use a singleton design pattern for the ToDoList so that there is only one
 * instance of the list at a time.
 *
 * The instance will exist as long as the application stays in memory,
 * so storing the list in a singleton will keep the data available no matter
 * what happens with activities, fragments and their lifecycles.
 *
 */
public class ToDoList {
    private static ToDoList sToDoList;
    private Context mContext;

    private ArrayList<ToDo> mToDos;

    // note: the constructor is private and will use the get() method
    // to retrieve an instance of ToDoList.
    // this is important to writing a singleton design pattern.
    private ToDoList(Context context){
        mContext = context;
        mToDos = new ArrayList<>();

        // some code to populate list with dummy todo items
        // tobe removed later.
        for( int i=0; i < 100; ++i ){
            ToDo t = new ToDo();
            t.setTitle("ToDo #" + i);
            t.setCompleted(i % 2 == 0);
            mToDos.add(t);
        }
    }

    public static ToDoList get(Context context){
        // if no instance, create it and return reference to it.
        if( sToDoList == null ){
            sToDoList = new ToDoList(context.getApplicationContext());  // pass in the Application context instead of any (Activity, Service) context.
        }
        return sToDoList;   // else there is already an instance of ToDoList, so just return ref to it.
    }

    public ArrayList<ToDo> getToDos(){
        return mToDos;
    }

    public ToDo getToDo(UUID id){
        for( ToDo t : mToDos ){
            if( t.getId().equals(id) ){
                return t;
            }
        }
        return null;
    }

}
