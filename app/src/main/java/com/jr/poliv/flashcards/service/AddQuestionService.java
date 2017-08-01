package com.jr.poliv.flashcards.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jr.poliv.flashcards.data.FlashCardContract;

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
    protected void onHandleIntent(Intent intent) {

        String question = intent.getStringExtra(QUESTION);
        String answer = intent.getStringExtra(ANSWER);

        ContentValues values = new ContentValues();

        values.put(FlashCardContract.FlashCardEntry.COLUMN_QUESTION, question);
        values.put(FlashCardContract.FlashCardEntry.COLUMN_ANSWER, answer);

        getContentResolver().insert(FlashCardContract.FlashCardEntry.CONTENT_URI, values);

    }
}
