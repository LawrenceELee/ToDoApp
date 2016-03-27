package com.example.lambda.todoapp;

import android.app.Fragment;
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
        mToDo = new ToDo();

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
        // we disable it for now, but will set it up to open date picker widget.
        mDateButton = (Button) view.findViewById(R.id.todo_date);
        mDateButton.setText(mToDo.getFormattedDate().toString());
        mDateButton.setEnabled(false);

        // wire up check box.
        mCompletedCheckBox = (CheckBox) view.findViewById(R.id.todo_completed);
        mCompletedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // set the todo task's completed property
                mToDo.setCompleted(true);
            }
        });


        return view;
    } // end onCreateView
}
