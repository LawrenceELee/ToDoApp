package com.example.lambda.todoapp;

//import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lawrence on 3/26/2016.
 *
 * ToDo.java is the Model for this app.
 *
 */
public class ToDo {

    // these are the keys for the json key-val pair
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_COMPLETED = "completed";
    private static final String JSON_DATE = "date";

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

    // read file (json) to model (arraylist)
    public ToDo(JSONObject json) throws JSONException{
        mId = UUID.fromString(json.getString(JSON_ID));
        if( json.has(JSON_TITLE) ){
            mTitle = json.getString(JSON_TITLE);
        }
        mCompleted = json.getBoolean(JSON_COMPLETED);
        mDate = new Date(json.getLong(JSON_DATE));      // time is store as a long number, need to convert to Date.
    }

    // write model from ArrayList to a file
    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();  // JSONObject does the hardwork of converting data to JSON
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_COMPLETED, mCompleted);
        json.put(JSON_DATE, mDate.getTime());
        return json;
    }

    @Override
    public String toString(){
        return mTitle;
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
