package com.example.lambda.todoapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;
import java.util.zip.CheckedInputStream;

/**
 * Created by Lawrence on 3/26/2016.
 *
 * ToDoFragment.java is the controller.
 *
 * It interacts with the View (layout).
 * It will have a View consisting of LinearLayout and an EditText and will have a listener that updates the model layer when text changes.
 * And interacts with Model by referencing a ToDo instance.
 *
 *
 */
public class ToDoFragment extends Fragment{

    public static final String EXTRA_TODO_ID = "com.example.lambda.todoapp.todointent.todo_id";
    public static final String DIALOG_DATE = "date";


    private ToDo mToDo;
    private EditText mTitleField;

    private Button mDateButton;
    private CheckBox mCompletedCheckBox;

    // Fragment.onCreate() works nearly the same as Activity.onCreate,
    // the only difference is that you can View.findViewById(int) on the fragment's view.
    //
    // Fragment class doesn't have a corresponding convenience method, so you have to call
    // the real thing.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we are retrieving the EXTRA info from the intent that was sent to ToDoFragment.
        //UUID todoId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_TODO_ID);
        // we use getArguments() now since we are using Bundles for the "extras" instead of Intents.
        UUID todoId = (UUID) getArguments().getSerializable(EXTRA_TODO_ID);


        // that extra info was the id of the todo object.
        mToDo = ToDoList.get(getActivity()).getToDo(todoId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate layout file into Java R's space.
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        // wire up this Controller with EditText field by initialing the EditText and
        // set a TextWatcher to update model if text is changed.
        // It is analogous to setting onClickListeners on Buttons.
        mTitleField = (EditText) view.findViewById(R.id.todo_title);        // have to use view.findView, can't just use this.findView...
        mTitleField.setText(mToDo.getTitle());      // change the textfield to match data in ToDo Model.
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mToDo.setTitle(charSequence.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // we don't use this method, so leave blank.
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // we don't use this method, so leave blank.
            }
        });

        // wire up mDateButton, set text to current date.
        mDateButton = (Button) view.findViewById(R.id.todo_date);
        mDateButton.setText(mToDo.getFormattedDate().toString());
        // show a date picker dialog when date button clicked.
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(fm, DIALOG_DATE);
            }
        });

        // wire up check box.
        mCompletedCheckBox = (CheckBox) view.findViewById(R.id.todo_completed);
        mCompletedCheckBox.setChecked(mToDo.isCompleted());         // mark the checkbox if it was set.
        mCompletedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // set the todo task's completed property
                mToDo.setCompleted(true);
            }
        });

        return view;
    } // end onCreateView


    // we're writing this method so that we don't break encapsulation between the host activity (ToDoActivity)
    // and the hostee fragment (ToDoFragment). We want to any activity to host a ToDoFragment, not just ToDoActivity.
    // to accomplish this, we use arguments bundles to "stash" extra info, instead of the "putExtra stash"
    // every fragment can have a Bundle object attach to it. Bundles contain key-val pairs that work just like the intent extras of an Activity.
    // each pair is known as an argument.
    public static ToDoFragment newInstance(UUID todoId){

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TODO_ID, todoId);

        ToDoFragment fragment = new ToDoFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
