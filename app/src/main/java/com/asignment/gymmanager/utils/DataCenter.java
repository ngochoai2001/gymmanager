package com.asignment.gymmanager.utils;

import com.asignment.gymmanager.model.Food;
import com.asignment.gymmanager.model.LineItem;
import com.asignment.gymmanager.model.Meal;
import com.asignment.gymmanager.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DataCenter {
    public static DataCenter data;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private Meal meal = new Meal();
    private Food f = new Food();
    private User user = new User();
    private ArrayList<Food> foods = new ArrayList<>();

//    public Meal getMeal(String date, String loai) {
//        Meal meal = new Meal();
//        return meal;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public void addFood(Food food) {
        DatabaseReference mRef = ref.child("Food").push();
        food.setId(mRef.getKey());
        mRef.setValue(food.toMap());


    }

    public void addMeal(Meal meal) {
        DatabaseReference mRef = ref.child(meal.getType()).child(meal.getDate());
        mRef.setValue(meal.toMap());
    }

    public void addLineitem(Meal m, LineItem l) {
        DatabaseReference mref = ref.child(m.getType()).child(m.getDate()).child("items");
        mref.updateChildren(l.toMap());
    }


    public void editMeal(Meal meal) {
        DatabaseReference mRef = ref.child(meal.getType()).child(meal.getDate());
        Map<String, Object> updateMeal = new HashMap<>();
        ArrayList<Map<String, Object>> listMap = new ArrayList<>();
        for (int i = 0; i < meal.getItems().size(); i++) {
            listMap.add(meal.getItems().get(i).toMap());
        }
        updateMeal.put("items", listMap);
        mRef.updateChildren(updateMeal);
    }

    public ArrayList<Food> getListFood() {
//        ArrayList<Food> foods = new ArrayList<>();
        ref.child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
//                    if(i.getKey().equals(date)){
//
//                    }
                    Food m = i.getValue(Food.class);
                    foods.add(m);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return foods;
    }

    public void editMealV2(Meal meal) {

        DatabaseReference mRef = ref.child(meal.getType()).child(meal.getDate());
        mRef.setValue(meal.toMap());
    }

    public Meal getMeal(final String date, String type) {
//        final Meal[] meal = {new Meal()};
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Breakfast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    Meal m = i.getValue(Meal.class);
                    if (m.getDate().equals(date)) {
                        meal = m;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return meal;
    }

    public Food getFood(final String id) {
//        final Meal[] meal = {new Meal()};
        ref.child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
//                    if(i.getKey().equals(date)){
//
//                    }
                    Food m = i.getValue(Food.class);
//                    if (m.getId().equals(id)) {
//                        f = m;
//                        break;
//                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return f;
    }

    public void deleteItem(String id, String date, String type) {
        DatabaseReference mRef = ref.child(type).child(date).child("items").child(0 + "");
        mRef.setValue(null);

//        Query query = ref.child(type).child(date).child("items");
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot i : dataSnapshot.getChildren()) {
//                    if (i.getKey().equals(idFood)) {
//                        i.getExerciseRef().removeValue();
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

}
