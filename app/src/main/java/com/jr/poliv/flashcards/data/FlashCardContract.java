package com.jr.poliv.flashcards.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by poliv on 7/31/2017.
 */

public class FlashCardContract {

    public static final String CONTENT_AUTHORITY = "com.jr.poliv.flashcards";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_FLASHCARDS = "flashcards";

    public static class FlashCardEntry implements BaseColumns{
        public static String TABLE_NAME = "flashcards";
        public static String COLUMN_QUESTION = "question";
        public static String COLUMN_ANSWER = "answer";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FLASHCARDS).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FLASHCARDS;
        public static String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FLASHCARDS;


    }
}
