package com.jr.poliv.flashcards.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RemoteViews;

import com.jr.poliv.flashcards.R;
import com.jr.poliv.flashcards.data.FlashCardContract;

import java.util.Random;

/**
 * Created by poliv on 8/3/2017.
 */

public class FlashCardWidget extends AppWidgetProvider {

    private final String intKey = "response";
    private final String stringKey = "text";
    private final int SWITCH_TO_ANSWER = 10;
    private final int SWITCH_TO_QUESTION = 100;
    private final int RANDOM_QUESTION = 1000;
    private int response;
    private String intentText;




    @Override
    public void onReceive(Context context, Intent intent) {


        response = intent.getIntExtra(intKey, 1000);

        switch (response){
            case SWITCH_TO_ANSWER:{
                intentText = intent.getStringExtra(stringKey);
                break;
            }
            case SWITCH_TO_QUESTION:{
                intentText = intent.getStringExtra(stringKey);
                break;
            }
            default:{
             break;
            }
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);


        final int N = appWidgetIds.length;

        for( int i = 0; i < N; i++){

            int appWidgetId = appWidgetIds[i];
            String text;

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent intent = new Intent(context, FlashCardWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});


            switch (response){
                case SWITCH_TO_ANSWER:{
                    text = getAnswer(context, intentText);
                    intent.putExtra(intKey, SWITCH_TO_QUESTION);
                    views.setTextViewText(R.id.btseeAnswer, "SEE QUESTION");
                    break;
                }
                case SWITCH_TO_QUESTION:{
                    text = getQuestion(context, intentText);
                    intent.putExtra(intKey, SWITCH_TO_ANSWER);
                    views.setTextViewText(R.id.btseeAnswer, "SEE ANSWER");

                    break;
                }
                default:{
                    text = getRandomQuestion(context);
                    intent.putExtra(intKey, SWITCH_TO_ANSWER);
                    views.setTextViewText(R.id.btseeAnswer, "SEE ANSWER");
                    break;
                }
            }
            intent.putExtra(stringKey, text);
            views.setTextViewText(R.id.edit_text, text);

            Intent intent2 = new Intent(context, FlashCardWidget.class);
            intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
            intent2.putExtra(intKey, RANDOM_QUESTION);
            intent2.putExtra(stringKey, text);


            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.btseeAnswer, pendingIntent);
            views.setOnClickPendingIntent(R.id.nextQuestion, pendingIntent2);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }


    private String getRandomQuestion(Context context){
        Cursor cursor = context.getContentResolver().query(FlashCardContract.FlashCardEntry.CONTENT_URI, new String[]{FlashCardContract.FlashCardEntry.COLUMN_QUESTION}, null, null, null);
        Random random = new Random();
        int position = random.nextInt(cursor.getCount());
        cursor.moveToPosition(position);
        return cursor.getString(0);
    }

    private String getAnswer(Context context, String question){
        String whereStatement = FlashCardContract.FlashCardEntry.COLUMN_QUESTION + " = ?";
        Cursor cursor = context.getContentResolver().query(FlashCardContract.FlashCardEntry.CONTENT_URI, new String[]{FlashCardContract.FlashCardEntry.COLUMN_ANSWER}, whereStatement, new String[]{question}, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }
    private String getQuestion(Context context, String answer){
        String whereStatement = FlashCardContract.FlashCardEntry.COLUMN_ANSWER + " = ?";
        Cursor cursor = context.getContentResolver().query(FlashCardContract.FlashCardEntry.CONTENT_URI, new String[]{FlashCardContract.FlashCardEntry.COLUMN_QUESTION}, whereStatement, new String[]{answer}, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }
}
