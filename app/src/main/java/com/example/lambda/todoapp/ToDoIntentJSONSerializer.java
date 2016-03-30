package com.example.lambda.todoapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Lawrence on 3/29/2016.
 *
 * This class is delegated to reading/writing the data model to a file
 * so that data can persist across different session of the app.
 *
 * Each app has a sandbox (/data/data/com.name.of.package) with space to write files to.
 * Only the app itself has permission to access it, others apps can't read others sandboxes.
 *
 * Benefits of self-contained code for serializing to JSON is that it is easily unit-tested
 * because it doesn't depend on much code in app. Also since it only takes a Context and filename,
 * it can be reused with anything that has a Context.
 *
 */
public class ToDoIntentJSONSerializer {

    private Context mContext;
    private String mFilename;

    public ToDoIntentJSONSerializer(Context c, String f){
        mContext = c;
        mFilename = f;
    }

    // code to save ArrayList to JSON file
    public void saveToDos(ArrayList<ToDo> todos)
            throws JSONException, IOException{

        // build an array in JSON
        JSONArray array = new JSONArray();
        for( ToDo t : todos ){
            array.put(t.toJSON());
        }

        // write file to disk
        Writer writer = null;

        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if( writer != null ){
                writer.close();
            }
        }
    }

    // code to load JSON file into ArrayList/app after app is killed.
    public ArrayList<ToDo> loadToDos() throws IOException, JSONException{
        ArrayList<ToDo> todos = new ArrayList<>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // line breaks are omitted and irrelevant
                jsonString.append(line);
            }

            // parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            // build the array of todos from JSONObjects
            for (int i = 0; i < array.length(); ++i) {
                todos.add(new ToDo(array.getJSONObject(i)));
            }

        } catch( FileNotFoundException e ){
            // ignore this error since there will be no file the first time this app is run.
        } finally {
            if( reader != null ) {
                reader.close();
            }
        }
        return todos;
    }


}
