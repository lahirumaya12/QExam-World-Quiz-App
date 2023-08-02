package com.example.qexamworld;

import static com.example.qexamworld.DbQuery.ANSWERED;
import static com.example.qexamworld.DbQuery.NOT_VISITED;
import static com.example.qexamworld.DbQuery.REVIEW;
import static com.example.qexamworld.DbQuery.UNANSWERED;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class QuestionGridAdapter extends BaseAdapter {

    private int numOfQues;
    private Context context;

    public QuestionGridAdapter(Context context, int numOfQues) {
        this.numOfQues = numOfQues;
        this.context = context;
    }

    @Override
    public int getCount() {
        return numOfQues;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View myview;
        if (view == null){
            myview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ques_grid_item, viewGroup, false);

        }else{
            myview = view;
        }

        myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (context instanceof QuestionsActivity)
                    ((QuestionsActivity)context).goToQuestion(i);

            }
        });

        TextView quesTV = myview.findViewById(R.id.ques_num);

        quesTV.setText(String.valueOf(i + 1));

        switch (DbQuery.g_quesList.get(i).getStatus()){

            case NOT_VISITED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.not_visited)));
                break;
            case UNANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.unanswer)));
                break;
            case ANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.answer)));
                break;
            case REVIEW:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(), R.color.review)));
                break;

            default:
                break;



        }

        return myview;
    }
}
