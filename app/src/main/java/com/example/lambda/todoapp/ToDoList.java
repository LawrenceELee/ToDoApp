package com.example.lambda.todoapp;

import android.content.Context;
import android.util.Log;

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

    private static final String TAG = "ToDoList";
    private static final String FILENAME = "todos.json";

    private static ToDoList sToDoList;
    private Context mContext;

    private ArrayList<ToDo> mToDos;
    private ToDoIntentJSONSerializer mSerializer;

    // note: the constructor is private and will use the get() method
    // to retrieve an instance of ToDoList.
    // this is important to writing a singleton design pattern.
    private ToDoList(Context context){
        mContext = context;
        mToDos = new ArrayList<>();
        mSerializer = new ToDoIntentJSONSerializer(mContext, FILENAME);
    }

    public static ToDoList get(Context context){
        // if no instance, create it and return reference to it.
        if( sToDoList == null ){
            sToDoList = new ToDoList(context.getApplicationContext());  // pass in the Application context instead of any (Activity, Service) context.
        }
        return sToDoList;   // else there is already an instance of ToDoList, so just return ref to it.
    }

    // code to save file will happen during the onPause() stage of lifecycle.
    public boolean saveToDos(){
        try{
            mSerializer.saveToDos(mToDos);
            Log.d(TAG, "todos saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving todos: ", e);
            return false;
        }
    }

    // add a todo to the arraylist
    public void addToDo(ToDo t){
        mToDos.add(t);
    }

    // return the list of todos
    public ArrayList<ToDo> getToDos(){
        return mToDos;
    }

    // helper to get the specific todo that mataches id.
    public ToDo getToDo(UUID id){
        for( ToDo t : mToDos ){
            if( t.getId().equals(id) ){
                return t;
            }
        }
        return null;
    }

}
