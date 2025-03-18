package com.example.mistakes_quizlets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    interface OptionClickListener {
        void onOptionClickListener(int viewId, int position);
    }

    private OptionClickListener mListener;
   private List<QuestionModel> questionsList;



   public QuestionAdapter(List<QuestionModel> questionsList){
       this.questionsList = questionsList;
   }
//
//    public void setOnOptionClickListener(OptionClickListener listener) {
//        this.mListener = listener;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_item_layout,viewGroup,false);

       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.setData(position);
        holder.ques.setText(questionsList.get(position).getQuestion());
        holder.optionA.setText(questionsList.get(position).getOptionA());
        holder.optionB.setText(questionsList.get(position).getOptionB());
        holder.optionC.setText(questionsList.get(position).getOptionC());
        holder.optionD.setText(questionsList.get(position).getOptionD());

        if (questionsList.get(position).getSelectedAns() == 1){
            holder.optionA.setSelected(true);
            holder.optionB.setSelected(false);
            holder.optionC.setSelected(false);
            holder.optionD.setSelected(false);
        }else if (questionsList.get(position).getSelectedAns() == 2){
            holder.optionA.setSelected(false);
            holder.optionB.setSelected(true);
            holder.optionC.setSelected(false);
            holder.optionD.setSelected(false);
        }else if (questionsList.get(position).getSelectedAns() == 3){
            holder.optionA.setSelected(false);
            holder.optionB.setSelected(false);
            holder.optionC.setSelected(true);
            holder.optionD.setSelected(false);
        }else if (questionsList.get(position).getSelectedAns() == 4){
            holder.optionA.setSelected(false);
            holder.optionB.setSelected(false);
            holder.optionC.setSelected(false);
            holder.optionD.setSelected(true);
        }

        holder.optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionsList.get(position).setSelectedAns(1);
                notifyItemChanged(position);
//                    if (mListener != null) {
//                        mListener.onOptionClickListener(view.getId(), pos);
//                    }
            }
        });

        holder.optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionsList.get(position).setSelectedAns(2);
                notifyItemChanged(position);
//                    if (mListener != null) {
//                        mListener.onOptionClickListener(view.getId(), pos);
//                    }
            }
        });

        holder.optionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionsList.get(position).setSelectedAns(3);
                notifyItemChanged(position);
//                    if (mListener != null) {
//                        mListener.onOptionClickListener(view.getId(), pos);
//                    }

            }
        });

        holder.optionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionsList.get(position).setSelectedAns(4);
                notifyItemChanged(position);
//                    if (mListener != null) {
//                        mListener.onOptionClickListener(view.getId(), pos);
//                    }

            }
        });
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private Button optionA, optionB, optionC, optionD, prevSelectedB;
        private TextView ques;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            ques = itemView.findViewById(R.id.tv_question);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            prevSelectedB = null;

        }
    }
}
