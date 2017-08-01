package com.jr.poliv.flashcards.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;


/**
 * Created by poliv on 7/31/2017.
 */

public class FlashCardDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "flashcard.db";
    private Context context;

    public FlashCardDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TODO_TABLE = "CREATE TABLE " + FlashCardContract.FlashCardEntry.TABLE_NAME + " (" +
                FlashCardContract.FlashCardEntry._ID + " INTEGER PRIMARY KEY autoincrement, " +
                FlashCardContract.FlashCardEntry.COLUMN_QUESTION + " TEXT NOT NULL, " +
                FlashCardContract.FlashCardEntry.COLUMN_ANSWER + " TEXT NOT NULL, " +
                "UNIQUE ( " + FlashCardContract.FlashCardEntry.COLUMN_QUESTION + ", " + FlashCardContract.FlashCardEntry.COLUMN_ANSWER + " ) ON " +
                "CONFLICT IGNORE" +
                " );";

        db.execSQL(SQL_CREATE_TODO_TABLE);

        if(!addRowsFromJson(db))
            Log.e("Paul", "Insert did not work");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + FlashCardContract.FlashCardEntry.TABLE_NAME);
        onCreate(db);

    }

    private boolean addRowsFromJson(SQLiteDatabase db) {
        try {
            JSONObject object = new JSONObject(new GetJson().execute().get());

            JSONArray array = object.getJSONArray("flashcards");

            return addRows(array, db);

        } catch (JSONException e) {
            Log.e("Paul", "JSONException " + e.toString());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.e("Paul", "InterruptedException " + e.toString());
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.e("Paul", "ExecutionException " + e.toString());
            e.printStackTrace();
        }


        return false;
    }



    private boolean addRows(JSONArray array, SQLiteDatabase db) throws JSONException {

        boolean b = false;

        try {
            db.beginTransaction();
            String sql = "INSERT INTO " + FlashCardContract.FlashCardEntry.TABLE_NAME + " (" + FlashCardContract.FlashCardEntry.COLUMN_QUESTION
                    + ", " + FlashCardContract.FlashCardEntry.COLUMN_ANSWER + ") VALUES (?, ?)";

            SQLiteStatement statement = db.compileStatement(sql);


            for (int i = 0; i < array.length(); i++) {
                statement.clearBindings();
                statement.bindString(1, array.getJSONObject(i).getString("question"));
                statement.bindString(2, array.getJSONObject(i).getString("answer"));
                statement.executeInsert();
            }
            db.setTransactionSuccessful();
            b = true;
        }catch (Exception e){
            Log.d("Paul", "Exception "+e.toString());
            e.printStackTrace();
        }finally {
            db.endTransaction();

        }
        return b;

    }

    private class GetJson extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... params) {
            return getFile();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    String getFile(){
        String json = null;
        try{
            InputStream is = context.getAssets().open("flashcards.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }


}
