package com.jr.poliv.flashcards;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jr.poliv.flashcards.data.FlashCardContract;

/**
 * Created by poliv on 7/31/2017.
 */

public class FlashCardCursorAdapter extends RecyclerView.Adapter<FlashCardCursorAdapter.ViewHolder> {

    private Cursor cursor;
    private GotoAnswerListener gotoAnswerListener;

    public FlashCardCursorAdapter(Context context, Cursor cursor){
        this.cursor = cursor;

       try{
            gotoAnswerListener = (GotoAnswerListener) context;


        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement GotoAnswerListener");

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.bindView(cursor);
    }


    @Override
    public int getItemCount() {

        return cursor !=null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView question;
        //int flashcardID = -1;

        private ViewHolder(View itemView) {
            super(itemView);
            question = (TextView) itemView.findViewById(R.id.tvQuestions);
            view = itemView;
        }


        private void bindView(Cursor cursor) {
            //flashcardID = cursor.getInt(cursor.getColumnIndex(FlashCardContract.FlashCardEntry._ID));
            final String question = cursor.getString(cursor.getColumnIndex(FlashCardContract.FlashCardEntry.COLUMN_QUESTION));
            final String answer = cursor.getString(cursor.getColumnIndex(FlashCardContract.FlashCardEntry.COLUMN_ANSWER));
            this.question.setText(question);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoAnswerListener.questionAnswer(question, answer);
                }
            });


        }
    }

    public interface GotoAnswerListener {
        void questionAnswer(String question, String answer);
    }
}
