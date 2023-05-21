package com.asignment.gymmanager.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class LineItem implements Serializable {
    private Food food;
    private int number;
    private String id;

    public LineItem(Food food, int number) {
        this.food = food;
        this.number = number;
    }

    public LineItem(Food food, int number, String id) {
        this.food = food;
        this.number = number;
        this.id = id;
    }

    public LineItem() {
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float totalCalo() {
        float t = (float) number * food.getCalo();
        return t;
    }

    public float totalProtetin() {
        float t = (float) number * food.getProtein();
        return t;
    }

    public float totalFat() {
        float t = (float) number * food.getFat();
        return t;
    }

    public float totalCab() {
        float t = (float) number * food.getCarb();
        return t;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> value = new HashMap<>();
        value.put("id", id);
        value.put("number", number);
        value.put("food", food.toMap());
        return value;
    }

}
