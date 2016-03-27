package com.example.lambda.todoapp;

import java.util.UUID;

/**
 * Created by Lawrence on 3/26/2016.
 *
 * ToDo.java is the Model for this app.
 *
 */
public class ToDo {

    private UUID mId;
    private String mTitle;

    public ToDo(){
        mId = UUID.randomUUID();
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
}
