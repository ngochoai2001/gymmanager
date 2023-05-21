package com.asignment.gymmanager.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Food implements Serializable {
    private String id;
    private String name;
    private String imageUrl;
    private String unit;
    private int count;
    private float calo;
    private float protein;
    private float fat;
    private float carb;

    public Food(String id, String name, String imageUrl, String unit, int count, float calo, float protein, float fat, float carb) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.unit = unit;
        this.count = count;
        this.calo = calo;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
    }

    public Food() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getCalo() {
        return calo;
    }

    public void setCalo(float calo) {
        this.calo = calo;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getCarb() {
        return carb;
    }

    public void setCarb(float carb) {
        this.carb = carb;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> value = new HashMap<>();
        value.put("id", id);
        value.put("name", name);
        value.put("imageUrl", imageUrl);
        value.put("protein", protein);
        value.put("unit", unit);
        value.put("count", count);
        value.put("calo", calo);
        value.put("carb", carb);
        value.put("fat", fat);
        return value;
    }
}
