package com.example.mistakes_quizlets;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.HttpException;

public class dbHelper extends SQLiteOpenHelper {

    public static int catCount;
    public static int userCount;
    public static List<CategoryModel> g_catList = new ArrayList<>();
    public static final String DBNAME = "Login.db";
    public static List<TestModel> testList = new ArrayList<>();
    public static List<QuestionModel> questionsList = new ArrayList<>();
    public static int g_selected_cat_index = 0;
    public static int g_selected_test_index = 0;

    public dbHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
//        MyDB.execSQL("create Table users(email TEXT primary key, password TEXT)");
        String sql = "create table user(id integer primary key autoincrement,email varchar(20),password varchar(20))";
        MyDB.execSQL(sql);

        String sql1 = "create table quiz(id integer primary key autoincrement,name varchar(20),no_of_tests int(20))";
        MyDB.execSQL(sql1);

        String sql2 = "create table tests_list(id integer primary key autoincrement, time int(20), top_score int(20),cat_index int(20))";
        MyDB.execSQL(sql2);

        String sql3 = "create table questions(id integer primary key autoincrement, question varchar(50), A varchar(20),B varchar(20),C varchar(20),D varchar(20), answer int(20), cat_index int(20), test_index int(20))";
        MyDB.execSQL(sql3);



    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists user");
        MyDB.execSQL("drop Table if exists quiz");
        MyDB.execSQL("drop Table if exists tests_list");
        MyDB.execSQL("drop Table if exists questions");
        onCreate(MyDB);
    }

    public Boolean insertQuestion(String question, String A, String B, String C, String D, int answer, int cat_index, int test_index){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", question);
        contentValues.put("A", A);
        contentValues.put("B", B);
        contentValues.put("C", C);
        contentValues.put("D", D);
        contentValues.put("answer", answer);
        contentValues.put("cat_index", cat_index);
        contentValues.put("test_index", test_index);

        long result = MyDB.insert("questions", null, contentValues);
        MyDB.close();
        if (result == -1) return false;
        else
            return true;
    }

    public Boolean insertTest(int time, int top_score, int cat_index){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", time);
        contentValues.put("top_score", top_score);
        contentValues.put("cat_index", cat_index);
        long result = MyDB.insert("tests_list", null, contentValues);
        MyDB.close();
        if (result == -1) return false;
        else
            return true;
    }

    public Boolean insertCat(String name, int no) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("no_of_tests", no);
        long result = MyDB.insert("quiz", null, contentValues);
        MyDB.close();
        if (result == -1) return false;
        else
            return true;
    }



    public Boolean insertData(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDB.insert("user", null, contentValues);
        MyDB.close();
        if (result == -1) return false;
        else
            return true;
    }

    public Boolean resetData(String email, String password) {

        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        int result = MyDB.update("user", contentValues, "email =?", new String[]{email});
        if (result == -1) return false;
        else
            return true;

    }

    public void updateCatNoT(int cat_index){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int noOfTests = g_catList.get(cat_index).getNoOfTests();

        String strSQL = "UPDATE quiz SET no_of_tests = g_catList.get(cat_index).getNoOfTests()+1 WHERE cat_index = "+ cat_index;

        MyDB.execSQL(strSQL);
//        contentValues.put("no_of_tests", noOfTests+1);
//        int result = MyDB.update("tests_list", contentValues, "cat_index =?", new String[]{String.valueOf(cat_index)});
//        if (result == -1) return false;
//        else
//            return true;
    }

    public Boolean checkemail(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "select * from user where email=?";
        Cursor cursor = MyDB.rawQuery(sql, new String[]{email});
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;

    }

    public Boolean checkusernamepassword(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        String sql = "select * from user where email=? and password=?";
        Cursor cursor = MyDB.rawQuery(sql, new String[]{email, password});
        if (cursor.moveToFirst()) {
            cursor.close();
            MyDB.close();
            return true;
        }
        MyDB.close();
        return false;
    }

    @SuppressLint("Range")
    public boolean loadCategories() {
        g_catList.clear();
        catCount =0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query("quiz", null, null, null, null, null, null);
        if (c != null && c.getCount() >= 1 ) {
            while (c.moveToNext()) {
                g_catList.add(new CategoryModel(c.getInt(c.getColumnIndex("id")), c.getString(c.getColumnIndex("name")),
                        c.getInt(c.getColumnIndex("no_of_tests"))));
                catCount+=1;

            }
            c.close();
            db.close();//关闭数据库
        }
        if (g_catList.size() > 0){
            return true;
        }else {
            return false;
        }
    }


    @SuppressLint("Range")
    public boolean loadTestData() {
        testList.clear();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query("tests_list", null, null, null, null, null, null);
        if (c != null && c.getCount() >= 1 ) {
            while (c.moveToNext()) {
                testList.add(new TestModel(c.getInt(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("top_score")),
                        c.getInt(c.getColumnIndex("time")),c.getInt(c.getColumnIndex("cat_index"))));
//            updateCatNoT(c.getInt(c.getColumnIndex("cat_index")));
            }
            c.close();
            db.close();//关闭数据库
        }
        if (g_catList.size() > 0){
            return true;
        }else {
            return false;
        }
    }

    @SuppressLint("Range")
    public void loadQuestionsData() {
        questionsList.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query("questions", null, null, null, null, null, null);
        if (c != null && c.getCount() >= 1 ) {
            while (c.moveToNext()) {


                if (c.getInt(c.getColumnIndex("test_index"))==g_selected_test_index&&c.getInt(c.getColumnIndex("cat_index"))==g_selected_cat_index){
                questionsList.add(new QuestionModel(c.getString(c.getColumnIndex("question")), c.getString(c.getColumnIndex("A")), c.getString(c.getColumnIndex("B")), c.getString(c.getColumnIndex("C")), c.getString(c.getColumnIndex("D")),
                        c.getInt(c.getColumnIndex("answer")),c.getInt(c.getColumnIndex("cat_index")),c.getInt(c.getColumnIndex("test_index")),-1));}
            }
            c.close();
            db.close();//关闭数据库


        }}
    }






