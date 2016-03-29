package com.example.lambda.todoapp;

import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Lawrence on 3/27/2016.
 * <p/>
 * This is part of the controller layer.
 */
public class ToDoListFragment extends ListFragment {

    private static final String TAG = ToDoListFragment.class.getSimpleName();

    private ArrayList<ToDo> mToDos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);    // tell fragment that we have an options/overflow menu.


        // getActivity() method returns the hosting activity and allows a fragment
        // to handle more of the activity's affairs.
        // Here we are using it to call the setTitle(int) to change what is displayed on
        // the actionbar to the title of the resource.
        getActivity().setTitle(R.string.todo_title_label);

        // get the singleton instance of the ToDoList.
        mToDos = ToDoList.get(getActivity()).getToDos();

        // using the custom adapter defined in inner class below.
        // we no longer use a generic ArrayAdapter<ToDo>
        ToDoAdapter adapter = new ToDoAdapter(mToDos);
        setListAdapter(adapter);

    } // end onCreate


    // this code tells app what to do if user clicks an item on the list
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ToDo t = ((ToDoAdapter)getListAdapter()).getItem(position);

        //Log.d(TAG, t.getTitle() + " was clicked"); // spot check to see if item clicker was working.

        // start a ToDoActivity when user clicks an item on the list.
        // we do so by creating an EXPLICIT intent.
        // (i.e. we "intent" to start a new ToDoActivity, given the current context.)
        //Intent intent = new Intent(getActivity(), ToDoActivity.class);
        // we are using a Pager now, so start Pager which manages the ToDoActivity
        // start ToDoPagerActivity with this ToDo
        Intent intent = new Intent(getActivity(), ToDoPagerActivity.class);

        intent.putExtra(ToDoFragment.EXTRA_TODO_ID, t.getId());     // putExtra is how we pass "info" from one activity to the next.
        // putExtra takes an unique TAG identifier and a Serializable object (which UUID's are).

        startActivity(intent);
    }

    // we implement onResume() so that when we press the back button from a ToDoActivity after making a change
    // the changes will appear on the ToDoListActivity (and not solely just update the model).
    // we can do this at the onResume (not the onStart) lifecycle.
    @Override
    public void onResume() {
        super.onResume();
        ( (ToDoAdapter)getListAdapter() ).notifyDataSetChanged();
    }

    // code to create the options/overflow menu (3 vertical dots on the upper right)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_todo_list, menu);
    }

    // using inner class to create an custom adapter for the list_item_todo view
    private class ToDoAdapter extends ArrayAdapter<ToDo> {

        // constructor
        public ToDoAdapter(ArrayList<ToDo> todos){
            super(getActivity(), 0, todos);
            // getActivity() provides context.
            // 0 for id means we aren't using a pre-defined layout.
            // we pass list of todos.
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a recycled view, inflate one.
            if( convertView == null ){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_todo, null);
            }

            // configure the view for this ToDo
            ToDo t = getItem(position);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.todo_list_item_titleTextView);
            titleTextView.setText(t.getTitle());

            TextView dateTextView =
                    (TextView) convertView.findViewById(R.id.todo_list_item_dateTextView);
            dateTextView.setText(t.getFormattedDate());

            CheckBox completedCheckBox =
                    (CheckBox) convertView.findViewById(R.id.todo_list_item_completedCheckBox);
            completedCheckBox.setChecked(t.isCompleted());

            return convertView;
        }
    } // end adapter inner class

}














