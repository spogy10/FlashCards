package com.jr.poliv.flashcards;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class QuestionAnswer extends AppCompatActivity {

    TextView question, answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        question = (TextView) findViewById(R.id.tvQuestion);
        answer = (TextView) findViewById(R.id.tvAnswer);

        Bundle extra = getIntent().getExtras();

        populateTextViews(extra);



    }

    private void populateTextViews(Bundle extra) {
        if(extra != null){

            question.setText(extra.getString("question"));
            answer.setText(extra.getString("answer"));

        }
    }
}
