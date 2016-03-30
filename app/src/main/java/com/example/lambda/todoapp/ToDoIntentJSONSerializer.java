package com.example.lambda.todoapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
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

    public void saveToDos(ArrayList<ToDo> todos)
            throws JSONException, IOException{

        // build an array in JSON
        JSONArray array = new JSONArray();
        for( ToDo t : todos ){
            array.put(t.toJSON());
        }

        // write file to disk
        Writer writer = null;

        // using Java 8's try-with-resources, finally close block not needed.
        try( OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE) ){
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }
    }


}
