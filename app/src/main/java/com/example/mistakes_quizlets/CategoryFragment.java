package com.example.mistakes_quizlets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends AppCompatActivity {

    private GridView catView;
    private List<CategoryModel> catList = new ArrayList<>();

    public CategoryFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_category, container,false);

        catView = view.findViewById(R.id.cat_Grid);


        loadCategories();
        CategoryAdapter adapter = new CategoryAdapter(catList);

        catView.setAdapter(adapter);

        return view;

    }

    private void loadCategories() {

        catList.clear();
        catList.add(new CategoryModel(1,"GK",20));
        catList.add(new CategoryModel(2,"HISTORY",30));
        catList.add(new CategoryModel(3,"SCIENCE",10));
        catList.add(new CategoryModel(4,"ENGLISH",25));
        catList.add(new CategoryModel(5,"MATHS",15));


    }


}
