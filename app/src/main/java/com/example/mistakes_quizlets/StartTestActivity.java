package com.example.mistakes_quizlets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StartTestActivity extends AppCompatActivity {

    private TextView catName, testNo, totalQ, bestScore, time;
    private Button startTestB;

    private ImageView backB;
    ArrayList<QuestionModel> qustionsL =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);


        qustionsL.add(new QuestionModel("1+1=?","1","2","3","4",1,1,1,0));
        qustionsL.add(new QuestionModel("1+2=?","1","2","3","4",1,1,0,0));
        qustionsL.add(new QuestionModel("1+3=?","1","2","3","4",1,1,0,0));
        qustionsL.add(new QuestionModel("1+4=?","1","2","3","4",1,1,0,0));




        init();


        MainActivity.DB.loadQuestionsData();
        setData();
    }

    private void init(){

        catName = findViewById(R.id.st_cat_name);
        testNo = findViewById(R.id.st_test_no);
        totalQ =findViewById(R.id.total_qustions);
        bestScore = findViewById(R.id.best_score);
        time = findViewById(R.id.time);
        startTestB = findViewById(R.id.start_testB);
        backB = findViewById(R.id.st_backB);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTestActivity.this.finish();
            }
        });


        startTestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTestActivity.this, QActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setData(){
        catName.setText(dbHelper.g_catList.get(dbHelper.g_selected_cat_index).getName());
        testNo.setText("Test No: "+String.valueOf(dbHelper.g_selected_test_index+1));
        totalQ.setText(String.valueOf(dbHelper.questionsList.size()));
        bestScore.setText(String.valueOf(dbHelper.testList.get(dbHelper.g_selected_test_index).getTopScore()));
        time.setText(String.valueOf(dbHelper.testList.get(dbHelper.g_selected_test_index).getTime()));

    }

}