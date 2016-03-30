package com.example.lambda.todoapp;

import android.os.Build;
import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
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
    private boolean mSubtitleVisible;


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

        // track subtitle during a screen rotation
        setRetainInstance(true);
        mSubtitleVisible = false;

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

        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if( mSubtitleVisible && showSubtitle != null ){
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    // When user presses a menu item in the options menu, the fragment relieves
    // a callback to the onOptionsItemSelected() method. This method receives an instance of
    // MenuItem that describes the user's selection.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch( item.getItemId() ){
            case R.id.menu_item_new_todo:
                // code to add a new todo task to the ToDoPagerActivity for the "+" menu item
                ToDo t = new ToDo();
                ToDoList.get(getActivity()).addToDo(t);
                Intent intent = new Intent(getActivity(), ToDoPagerActivity.class);
                intent.putExtra(ToDoFragment.EXTRA_TODO_ID, t.getId());
                startActivityForResult(intent, 0);
                return true;
            case R.id.menu_item_show_subtitle:
                // show subtitle if there is an action bar and room on it
                if( getActivity().getActionBar().getSubtitle() == null ){
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible = true;
                    item.setTitle(R.string.hide_subtitle);
                } else {
                    getActivity().getActionBar().setSubtitle(null);
                    mSubtitleVisible = false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ){
            if( mSubtitleVisible ){
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }

        // register ListView for a context menu
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        registerForContextMenu(listView);


        return view;
    }

    // callback code for the context menu (to delete a todo task)
    // similar to onCreateOptionsMenu() except you don't pass it an instance of MenuInflater so you have to get the
    // MenuInflater associated with ToDoListActivity.
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.todo_list_item_context, menu);
    }

    // callback that responds to the menu item selection
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // need to know which of the todos to delete from the fragment.
        // call to determine which todos was "long-press" to get context menu up.
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        ToDoAdapter adapter = (ToDoAdapter) getListAdapter();
        ToDo todo = adapter.getItem(position);

        switch( item.getItemId() ){
            case R.id.menu_item_delete_todo:
                ToDoList.get(getActivity()).deleteTodo(todo);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
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














