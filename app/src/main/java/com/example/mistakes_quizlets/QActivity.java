package com.example.mistakes_quizlets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.stetho.common.LogUtil;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class QActivity extends AppCompatActivity {    private RecyclerView questionView;
    private TextView tvQuesID, timerTV, catNameTV;
    private Button submitB, markB, clearSelB, prevSelectedB;
    private ImageButton prevQuesB, nextQuesB;
    private ImageView quesListB;
    QuestionAdapter questionAdapter;

    private int quesID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qactivity);
        LogUtil.e("QActivity 答题");
        prevSelectedB = null;

            init();

            //                DB.insertTest(10,20,1); //time,topscore,cat
//                DB.insertTest(5,15,1);
//                DB.insertTest(10,30,1);
//                DB.insertTest(50,5,2);




//        insertCat("GK", 10);

//        questionAdapter = new QuestionAdapter(Arrays.asList(QuestionModel.qustionsL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionView.setLayoutManager(layoutManager);
        setSnapHelper();
        questionAdapter = new QuestionAdapter(dbHelper.questionsList);

        questionView.setAdapter(questionAdapter);
//        questionAdapter.setOnOptionClickListener(new QuestionAdapter.OptionClickListener() {
//            @Override
//            public void onOptionClickListener(int viewId, int position) {
//                switch (viewId) {
//                    case R.id.optionA:
//                        selectOption(findViewById(viewId),1,position);
//
//                        break;
//                    case R.id.optionB:
//                        selectOption(findViewById(viewId),2,position);
//                        break;
//                    case R.id.optionC:
//                        selectOption(findViewById(viewId),3,position);
//                        break;
//                    case R.id.optionD:
//                        selectOption(findViewById(viewId),4,position);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });



        setClickListeners();

        startTimer();
}

    private void startTimer() {

        long time = dbHelper.testList.get(dbHelper.g_selected_test_index).getTime()*60*1000;

        CountDownTimer timer = new CountDownTimer(time,1000) {
            @Override
            public void onTick(long l) {
                String time1 = String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))

                        );

                timerTV.setText(time1);

            }

            @Override
            public void onFinish() {



            }
        };

        timer.start();



    }

    private void selectOption(Button btn, int option_num, int quesID) {
        if(prevSelectedB==null){
            btn.setBackgroundResource(R.drawable.selected_btn);
            QuestionModel.qustionsL[quesID].setSelectedAns(option_num);

            prevSelectedB = btn;

        }else{
            if(prevSelectedB.getId() == btn.getId()){
                btn.setBackgroundResource(R.drawable.unselectedo_btn);
                QuestionModel.qustionsL[quesID].setSelectedAns(-1);

                prevSelectedB = null;
            }

            else{
                prevSelectedB.setBackgroundResource(R.drawable.unselectedo_btn);
                btn.setBackgroundResource(R.drawable.selected_btn);
                QuestionModel.qustionsL[quesID].setSelectedAns(option_num);
                prevSelectedB = btn;
            }

        }

//        QuestionAdapter.notifyDataSetChanged();
    }

    private void setClickListeners() {
        prevQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(quesID > 0){
                    questionView.smoothScrollToPosition(quesID-1);
                }

            }
        });

        nextQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(quesID < dbHelper.questionsList.size()-1){
                    questionView.smoothScrollToPosition(quesID+1);
                }

            }
        });

        clearSelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.questionsList.get(quesID).setSelectedAns(-1);
                questionAdapter.notifyDataSetChanged();
            }
        });
    }

    private void init(){
        questionView = findViewById(R.id.questions_view);
        tvQuesID = findViewById(R.id.tv_quesID);
        timerTV = findViewById(R.id.tv_timer);
        catNameTV = findViewById(R.id.qa_catName);
        submitB = findViewById(R.id.submitB);
        markB = findViewById(R.id.markB);
        clearSelB = findViewById(R.id.clearSelB);
        prevQuesB = findViewById(R.id.prev_quesB);
        nextQuesB = findViewById(R.id.next_quesB);
        quesListB = findViewById(R.id.ques_list_gridB);

        quesID = 0;
        tvQuesID.setText("1/" +String.valueOf(dbHelper.questionsList.size()));
        catNameTV.setText(dbHelper.g_catList.get(dbHelper.g_selected_cat_index).getName());

    }

    private void setSnapHelper() {

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionView);

        questionView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID = recyclerView.getLayoutManager().getPosition(view);

                tvQuesID.setText(String.valueOf(quesID+1)+"/"+String.valueOf(dbHelper.questionsList.size()));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }




}