package com.example.mistakes_quizlets;

public class TestModel {

    private int testID;
    private int topScore;
    private int time;
    private int cat_index;

    public TestModel(int testID, int topScore, int time, int cat_index){
        this.testID = testID;
        this.time = time;
        this.topScore  = topScore;
        this.cat_index = cat_index;

    }

    public int getCat_index() {
        return cat_index;
    }

    public void setCat_index(int cat_index) {
        this.cat_index = cat_index;
    }

    public int getTestID() {
        return testID;
    }

    public int getTime() {
        return time;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public void setTime(int time) {
        this.time = time;

    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }
}


