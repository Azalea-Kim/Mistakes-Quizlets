package com.example.mistakes_quizlets;

import java.util.ArrayList;

public class QuestionModel {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correctAns;//正确答案
    private int cat_index;//类
    private int test_index;//题
    private int selectedAns;//目前选择的选项

    public static QuestionModel[] qustionsL = new QuestionModel[]{
            new QuestionModel("1+1=?", "1", "2", "3", "4", 1, 1, 0, -1),
            new QuestionModel("1+2=?","1","2","3","4",1,1,0,-1)
            ,new QuestionModel("1+3=?","1","2","3","4",1,1,0,-1),
            new QuestionModel("1+4=?","1","2","3","4",1,1,0,-1)};

//            qustionsL.add(new QuestionModel("1+1=?","1","2","3","4",1,1,1,0));
//            qustionsL.add(new QuestionModel("1+2=?","1","2","3","4",1,1,0,0));
//            qustionsL.add(new QuestionModel("1+3=?","1","2","3","4",1,1,0,0));
//            qustionsL.add(new QuestionModel("1+4=?","1","2","3","4",1,1,0,0));




    public QuestionModel(String question, String optionA, String optionB, String optionC, String optionD, int correctAns, int cat_index, int test_index, int selectedAns){
        this.question = question;
        this.optionA =optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAns = correctAns;
        this.cat_index = cat_index;
        this.test_index = test_index;
        this.selectedAns = selectedAns;
    }


    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public int getCorrectAns(){
        return correctAns;
    }

    public void setSelectedAns(int i){
        selectedAns = i;
    }

    public int getSelectedAns(){
        return selectedAns;
    }
}
