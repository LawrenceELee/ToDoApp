package com.example.lambda.todoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by Lawrence on 3/28/2016.
 * <p/>
 * This is part of the controller layer.
 * <p/>
 * It will be responsible for the dialog menu that appears whe you click the date button.
 */
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.example.lambda.todoapp.date";

    private Date mDate;

    // create the dialog menu for date picking
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date) getArguments().getSerializable(EXTRA_DATE);

        // create a calendar to get year, month, day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // this is the view with the numbers and up/down arrows
        // we inflate a view instead of using a DatePicker object b/c it makes updating easier.
        // we can just update the layout to whatever new thing we want (for example, a TimePicker from DatePicker).
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date, null);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                // translate year, month, day into a Date obj using a calendar
                // uses the arguments of onDateChanged() method, not the outter var year = calendar.get(...)
                mDate = new GregorianCalendar(year, month, day).getTime();

                // update argument to preserve selected value on screen rotation
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });

        // this the the dialog box with title, "Due date" text, and "Ok" button
        return new AlertDialog.Builder(getActivity())
                .setView(view)          // add the view to the dialog box.
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();


    }

    // logic to return date picked in date picker to ToDoFragment
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);

        return datePickerFragment;
    }

    // this method sends data to the target fragment from the date picker
    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)            return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
