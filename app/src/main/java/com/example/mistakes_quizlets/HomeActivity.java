package com.example.mistakes_quizlets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;

public class HomeActivity extends AppCompatActivity {

    Button textR, btnProfile;
    LottieAnimationView lottie;
//    private FrameLayout main_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textR = findViewById(R.id.textRecog);

        btnProfile = findViewById(R.id.profile);
        lottie = findViewById(R.id.lottie);

        lottie.animate().setDuration(2000).setStartDelay(2900);

//        main_frame = findViewById(R.id.main_frame);

        textR.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, TextRecognitionActivity.class )));

        btnProfile.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, QuizMenuActivity.class )));

//        setFragment(new QuizMenuFragment());

    }

//    private void setFragment(Fragment fragment) {
//        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
//        transaction.replace(main_frame.getId(), fragment);
//        transaction.commit();
//    }
}