package com.asignment.gymmanager.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Statistic {
    int totalGoal;
    private String date;
    private float totalFood, totalExercise, totalRemain, totalBreakfast, totalLunch, totalDinner, totalSnack;
    private ArrayList<Practice> listPractice = new ArrayList<>();

    public Statistic(String date, float totalExercise, int totalGoal, float totalRemain, float totalBreakfast, float totalLunch, float totalDinner, float totalSnack, ArrayList<Practice> listPractice) {
        this.date = date;
        this.totalFood = totalBreakfast + totalDinner + totalLunch + totalSnack;
        this.totalExercise = totalExercise;
        this.totalGoal = totalGoal;
        this.totalRemain = totalRemain;
        this.totalBreakfast = totalBreakfast;
        this.totalLunch = totalLunch;
        this.totalDinner = totalDinner;
        this.totalSnack = totalSnack;
        this.listPractice = listPractice;
    }

    public Statistic() {
    }

    public int getTotalGoal() {
        return totalGoal;
    }

    public void setTotalGoal(int totalGoal) {
        this.totalGoal = totalGoal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTotalFood() {
        return totalFood;
    }

    public void setTotalFood(float totalFood) {
        this.totalFood = totalFood;
    }

    public float getTotalExercise() {
        return totalExercise;
    }

    public void setTotalExercise(float totalExercise) {
        this.totalExercise = totalExercise;
    }

    public float getTotalRemain() {
        return totalRemain;
    }

    public void setTotalRemain(float totalRemain) {
        this.totalRemain = totalRemain;
    }

    public float getTotalBreakfast() {
        return totalBreakfast;
    }

    public void setTotalBreakfast(float totalBreakfast) {
        this.totalBreakfast = totalBreakfast;
    }

    public float getTotalLunch() {
        return totalLunch;
    }

    public void setTotalLunch(float totalLunch) {
        this.totalLunch = totalLunch;
    }

    public float getTotalDinner() {
        return totalDinner;
    }

    public void setTotalDinner(float totalDinner) {
        this.totalDinner = totalDinner;
    }

    public float getTotalSnack() {
        return totalSnack;
    }

    public void setTotalSnack(float totalSnack) {
        this.totalSnack = totalSnack;
    }

    public ArrayList<Practice> getListPractice() {
        return listPractice;
    }

    public void setListPractice(ArrayList<Practice> listPractice) {
        this.listPractice = listPractice;
    }
}
