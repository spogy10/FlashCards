package com.jr.poliv.flashcards.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by poliv on 7/31/2017.
 */

public class AddQuestionService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public static final String QUESTION = "QUESTION";
    public static final String ANSWER = "ANSWER";
    public AddQuestionService(String name) {

        super(name);
    }

    public AddQuestionService(){
        super("AddQuestionService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String question = intent.getExtras().getString(QUESTION);
        String answer = intent.getExtras().getString(ANSWER);

        //TODO: insert code to add these to databse

    }
}
