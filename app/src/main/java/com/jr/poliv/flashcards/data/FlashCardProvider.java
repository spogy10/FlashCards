package com.jr.poliv.flashcards.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by poliv on 7/31/2017.
 */

public class FlashCardProvider extends ContentProvider {

    private static final int FLASHCARD = 100;
    private static final int FLASHCARD_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private FlashCardDBHelper helper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FlashCardContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, FlashCardContract.PATH_FLASHCARDS, FLASHCARD);
        uriMatcher.addURI(authority, FlashCardContract.PATH_FLASHCARDS + "/#", FLASHCARD_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        helper = new FlashCardDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {

            //to-do
            case FLASHCARD: {
                cursor = helper.getReadableDatabase().query(FlashCardContract.FlashCardEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            //to-do by id
            case FLASHCARD_ID: {
                cursor = helper.getReadableDatabase().query(
                        FlashCardContract.FlashCardEntry.TABLE_NAME,
                        projection,
                        FlashCardContract.FlashCardEntry._ID + " = " + ContentUris.parseId(uri) + "",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch(match) {
            case FLASHCARD: {

                long _id = db.insert(FlashCardContract.FlashCardEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = FlashCardContract.FlashCardEntry.CONTENT_URI;
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        switch(match) {
            case FLASHCARD:
                rowsDeleted = db.delete(FlashCardContract.FlashCardEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        //Because null deletes all rows and db.delete would return 0 in this case
        if (selection == null || rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = helper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch(match) {
            case FLASHCARD:
                rowsUpdated = db.update(FlashCardContract.FlashCardEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
