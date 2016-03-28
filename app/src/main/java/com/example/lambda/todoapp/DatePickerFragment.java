package com.example.lambda.todoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * Created by Lawrence on 3/28/2016.
 *
 * This is part of the controller layer.
 *
 * It will be responsible for the dialog menu that appears whe you click the date button.
 *
 */
public class DatePickerFragment extends DialogFragment{

    // create the dialog menu for date picking
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // this is the view with the numbers and up/down arrows
        // we inflate a view instead of using a DatePicker object b/c it makes updating easier.
        // we can just update the layout to whatever new thing we want (for example, a TimePicker from DatePicker).
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date, null);

        // this the the dialog box with title, "Due date" text, and "Ok" button
        return new AlertDialog.Builder(getActivity())
                .setView(view)          // add the view to the dialog box.
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
