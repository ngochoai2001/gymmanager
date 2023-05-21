package com.asignment.gymmanager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class Meal implements Serializable {
    private String id;
    private String type;
    private ArrayList<LineItem> items;
    private String date;

    public Meal() {
        items = new ArrayList<LineItem>();
    }

    public Meal(String id, String type, ArrayList<LineItem> items, String date) {
        this.id = id;
        this.type = type;
        this.items = items;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<LineItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<LineItem> items) {
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return items.size();
    }

    public void addItem(LineItem item) {
        int code = Integer.parseInt(item.getFood().getId());
        int number = item.getNumber();
        for (int i = 0; i < items.size(); i++) {
            LineItem lineItem = items.get(i);
            if (Integer.parseInt(lineItem.getFood().getId()) == code) {
                lineItem.setNumber(number);
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(LineItem item) {
    }

    public void addLineitem(LineItem l) {
        int number = l.getNumber();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getFood().getId().equals(l.getFood().getId())) {
                items.get(i).setNumber(number + items.get(i).getNumber());
                return;
            }
        }
        items.add(l);
    }

    public float getTotalCalo() {
        float t = 0;
        for (int i = 0; i < getCount(); i++) {
            t = t + getItems().get(i).totalCalo();

        }
        return t;
    }

    public float getTotalFat() {
        float t = 0;
        for (int i = 0; i < getCount(); i++) {
            t = t + getItems().get(i).totalFat();

        }
        return t;
    }

    public float getTotalPro() {
        float t = 0;
        for (int i = 0; i < getCount(); i++) {
            t = t + getItems().get(i).totalProtetin();

        }
        return t;
    }

    public float getTotalCarb() {
        float t = 0;
        for (int i = 0; i < getCount(); i++) {
            t = t + getItems().get(i).totalCab();

        }
        return t;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> value = new HashMap<>();
        value.put("id", id);
        value.put("items", items);
        value.put("date", date);
        value.put("type", type);
        return value;
    }

}
