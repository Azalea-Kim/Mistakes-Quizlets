package com.example.mistakes_quizlets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class QuizMenuActivity extends AppCompatActivity {

    private GridView catView;
    public static List<CategoryModel> catList;
    private CategoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category);

        catView = findViewById(R.id.cat_Grid);


        catList = loadCategories();
        adapter = new CategoryAdapter(dbHelper.g_catList);

        catView.setAdapter(adapter);



    }


    private List<CategoryModel> loadCategories() {
        List<CategoryModel> catList = new ArrayList<>();

        catList.clear();
        catList.add(new CategoryModel(1,"GK",20));
        catList.add(new CategoryModel(2,"HISTORY",30));
        catList.add(new CategoryModel(3,"SCIENCE",10));
        catList.add(new CategoryModel(4,"ENGLISH",25));
        catList.add(new CategoryModel(5,"MATHS",15));

        return catList;


    }
}