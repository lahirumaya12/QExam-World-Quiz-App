package com.example.qexamworld;

import static com.example.qexamworld.DbQuery.ANSWERED;
import static com.example.qexamworld.DbQuery.REVIEW;
import static com.example.qexamworld.DbQuery.UNANSWERED;
import static com.example.qexamworld.DbQuery.g_quesList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qexamworld.Models.QuestionModel;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<QuestionModel> questionList;

    public QuestionsAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(i);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ques;
        private Button optionA, optionB, optionC, optionD, prevSelectedB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ques = itemView.findViewById(R.id.tv_question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);

            prevSelectedB = null;
        }

        private void setData(final int pos) {
            ques.setText(questionList.get(pos).getQuestion());
            optionA.setText(questionList.get(pos).getOptionA());
            optionB.setText(questionList.get(pos).getOptionB());
            optionC.setText(questionList.get(pos).getOptionC());
            optionD.setText(questionList.get(pos).getOptionD());


            setOption(optionA, 1, pos);
            setOption(optionB, 2, pos);
            setOption(optionC, 3, pos);
            setOption(optionD, 4, pos);

            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectOption(optionA, 1, pos);
                }
            });

            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionB, 2, pos);

                }
            });

            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionC, 3, pos);


                }
            });

            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionD, 4, pos);


                }
            });
        }

        private void  selectOption(Button btn, int option_num, int qursID){


            if (prevSelectedB == null ){
                btn.setBackgroundResource(R.drawable.selected_btn);
                DbQuery.g_quesList.get(qursID).setSelectAns(option_num);

                changeStatus(qursID, ANSWERED);
                prevSelectedB = btn;
            }else {

                if (prevSelectedB.getId() == btn.getId()){
                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    DbQuery.g_quesList.get(qursID).setSelectAns( -1);


                    changeStatus(qursID, UNANSWERED);
                    prevSelectedB = null;
                }else {

                    prevSelectedB.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);

                    DbQuery.g_quesList.get(qursID).setSelectAns(option_num);

                    changeStatus(qursID, ANSWERED);

                    prevSelectedB = btn;

                }
            }

        }

        private void changeStatus(int id, int status){
            if (g_quesList.get(id).getStatus() != REVIEW){
                g_quesList.get(id).setStatus(status);
            }
        }

        private void setOption(Button btn, int option_num, int quesID){

            if (DbQuery.g_quesList.get(quesID).getSelectAns() == option_num){
                btn.setBackgroundResource(R.drawable.selected_btn);
            }else {
                btn.setBackgroundResource(R.drawable.unselected_btn);
            }
        }
    }


}
