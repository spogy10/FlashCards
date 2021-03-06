package com.jr.poliv.flashcards.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import com.jr.poliv.flashcards.R;
import com.jr.poliv.flashcards.service.AddQuestionService;

/**
 * Created by poliv on 7/31/2017.
 */

public class AddQuestionDialogFragment extends DialogFragment {

    public static AddQuestionDialogFragment getInstance(){
        return new AddQuestionDialogFragment();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.dialog_layout);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText question = (EditText) ((AlertDialog) dialog).findViewById(R.id.edQuestion);
                EditText answer = (EditText) ((AlertDialog) dialog).findViewById(R.id.edAnswer);


                if( !(question.getText().toString().equals("") || question.getText() == null) && !(answer.getText().toString().equals("")) || answer.getText() == null) {
                    Intent intent = new Intent(getContext(), AddQuestionService.class);
                    intent.putExtra(AddQuestionService.QUESTION, question.getText().toString());
                    intent.putExtra(AddQuestionService.ANSWER, answer.getText().toString());
                    getActivity().startService(intent);
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();


    }
}
