package com.example.lambda.todoapp;

//import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import android.text.format.DateFormat;

/**
 * Created by Lawrence on 3/26/2016.
 *
 * ToDo.java is the Model for this app.
 *
 */
public class ToDo {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mCompleted;

    public ToDo(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    /*
    // You can format date using this way or the other way below.
    // note: don't use .toString() with this one, since it already returns a string.
    public String getFormattedDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d yyyy");
        return formatter.format(mDate);
    }
    */
    // need to use .toString since it returns a char sequence.
    public CharSequence getFormattedDate(){
        DateFormat dateFormat = new DateFormat();
        return dateFormat.format("EEE, MMM d yyyy", mDate);
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
