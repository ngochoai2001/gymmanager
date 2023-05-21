package com.asignment.gymmanager.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class WorkoutExercise {
    private String id;
    private String name;
    private float kalo;
    private int set;
    private String unit;
    private int quantity;
    private String content;
    private boolean checked;
    private String exerciseRef;

    public WorkoutExercise() {
    }

    public WorkoutExercise(String id, String name, float kalo, int set, String unit, int quantity, String content, boolean checked, String exerciseRef) {
        this.id = id;
        this.name = name;
        this.kalo = kalo;
        this.set = set;
        this.unit = unit;
        this.quantity = quantity;
        this.content = content;
        this.checked = checked;
        this.exerciseRef = exerciseRef;
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

    public float getKalo() {
        return kalo;
    }

    public void setKalo(float kalo) {
        this.kalo = kalo;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getExerciseRef() {
        return exerciseRef;
    }

    public void setExerciseRef(String exerciseRef) {
        this.exerciseRef = exerciseRef;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> value = new HashMap<>();
        value.put("id", id);
        value.put("name", name);
        value.put("kalo", kalo);
        value.put("set", set);
        value.put("quantity", quantity);
        value.put("unit", unit);
        value.put("exerciseRef", exerciseRef);
        return value;
    }

}
