package com.example.mistakes_quizlets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private RecyclerView testView;
    private Toolbar toolbar;
    private TestsAdapter adapter;
    private ArrayList<TestModel> thisTest = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        int cat_index = dbHelper.g_selected_cat_index;

//        int cat_index = getIntent().getIntExtra("CAT_INDEX",0);

        //!!!!

        getSupportActionBar().setTitle(dbHelper.g_catList.get(cat_index).getName());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        testView = findViewById(R.id.test_recycler_view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);


//        loadTestData();


        for(TestModel testModel : dbHelper.testList){
            if(testModel.getCat_index() == dbHelper.g_selected_cat_index){
                thisTest.add(testModel);
            }
        }



        adapter = new TestsAdapter(thisTest);
        testView.setAdapter(adapter);






    }



//    private void loadTestData() {
//        testList = new ArrayList<>();
//        testList.add(new TestModel("1", 50,20));
//        testList.add(new TestModel("2", 90,30));
//        testList.add(new TestModel("3", 0,25));
//        testList.add(new TestModel("4", 20,50));
//
//
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        if(item.getItemId() == android.R.id.home){
            TestActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);

    }
}