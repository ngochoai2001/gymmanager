package com.asignment.gymmanager.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.asignment.gymmanager.R;
import com.asignment.gymmanager.adapter.StatisticAdapter;
import com.asignment.gymmanager.model.Practice;
import com.asignment.gymmanager.model.Statistic;
import com.asignment.gymmanager.model.WorkoutExercise;
import com.asignment.gymmanager.utils.MethodUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class StatisticFragment extends Fragment implements StatisticAdapter.onCheckedChangeListener {
    private TextView tv_goal, tv_food, tv_excer, tv_remain, tv_breakfast, tv_lunch, tv_dinner, tv_snack, tv_excer_2, tv_food_2;
    private TextView tv_date, tv_title;
    private ImageView iv_back, iv_next;
    private String date = "", time = "";

    private ListView lvPractice;
    private ArrayList<Practice> listPractices = new ArrayList<>();
    private StatisticAdapter adapter;
    private int goal = 0;
    private Statistic statistic = new Statistic();
    private float totalExcer = 0;
    private float totalFood = 0;
    private float totalRemain = 0;
    private float totalBreak = 0, totalLunch = 0, totalDinner = 0, totalSnak = 0;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    public static StatisticFragment newInstance() {
        StatisticFragment fragment = new StatisticFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        MainActivity.state = ConstantUtils.FRAGMENT_STATISTIC;
//        ((MainActivity) getActivity()).updateTitle(MainActivity.page, MainActivity.state);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_statistic, container, false);
//        ((MainActivity) getActivity()).updateActionbar(false, false);
        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
//       WorkoutExercise workoutE = new WorkoutExercise("A", "A", 1, 1, "a", 1, "a", false, "");
//        Practice a = new Practice();
//        a.setChecked(false);
//        a.setWorkoutExercise(workoutE);
//        DatabaseReference mr = ref.child(("listPractice")).child("19-05-2017").child(a.getWorkoutExercise().getName());
//        mr.setValue(a);
        final MethodUtils methodUtils = new MethodUtils();
        date = methodUtils.getTimeNow();
//
        if (date.equals(methodUtils.getTimeNow())) {
            tv_date.setText("Today");
        } else {
            tv_date.setText(date);
        }
        updateUI();
        lvPractice = (ListView) getView().findViewById(R.id.lv_statistic_list_exercise);
        adapter = new StatisticAdapter(getActivity(), listPractices, date, this);
        lvPractice.setAdapter(adapter);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference re = FirebaseDatabase.getInstance().getReference();

        re.child("User").child(user.getUid()).child("goal").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                goal = dataSnapshot.getValue(Integer.class);
                tv_goal.setText(goal + "");
                DatabaseReference mref = ref.child(("Statistic")).child(date).child("totalGoal");
                mref.setValue(goal);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ref.child("Statistic").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                statistic = dataSnapshot.getValue(Statistic.class);
                totalBreak = statistic.getTotalBreakfast();
                totalDinner = statistic.getTotalDinner();
                totalLunch = statistic.getTotalLunch();
                totalSnak = statistic.getTotalSnack();
                totalFood = totalBreak + totalDinner + totalLunch + totalSnak;
                tv_breakfast.setText(totalBreak + "");
                tv_lunch.setText(totalLunch + "");
                tv_snack.setText(totalSnak + "");
                tv_dinner.setText(totalDinner + "");
                tv_food.setText(totalFood + "");
                tv_food_2.setText(totalFood + " Calories");
                totalRemain = goal - totalFood + totalExcer;
                tv_remain.setText(totalRemain + "");
                DatabaseReference mref = ref.child(("Statistic")).child(date).child("totalRemain");
                mref.setValue(totalRemain);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref.child("listPractice").child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        Practice p = i.getValue(Practice.class);
                        if (!p.getWorkoutExercise().getName().equals("A")) {
                            listPractices.add(p);
                            if (p.isChecked()) {
                                totalExcer = totalExcer + p.getWorkoutExercise().getKalo();
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    tv_excer.setText(totalExcer + "");
                    tv_excer_2.setText(totalExcer + " Calories");
                    totalRemain = goal - totalFood + totalExcer;
                    tv_remain.setText(totalRemain + "");
                    DatabaseReference mref = ref.child(("Statistic")).child(date).child("totalRemain");
                    mref.setValue(totalRemain);
                    DatabaseReference mre = ref.child(("Statistic")).child(date).child("totalExercise");
                    mre.setValue(totalExcer);
                } else {

                    ref.child("listPractice").child(methodUtils.UpDownDay(date, -1)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listPractices.clear();
                            WorkoutExercise workoutE = new WorkoutExercise("A", "A", 1, 1, "a", 1, "a", false, "");
                            Practice a = new Practice();
                            a.setChecked(false);
                            a.setWorkoutExercise(workoutE);
                            DatabaseReference mr = ref.child(("listPractice")).child(date).child(a.getWorkoutExercise().getName());
                            mr.setValue(a);
                            for (DataSnapshot i : dataSnapshot.getChildren()) {
                                Practice p = i.getValue(Practice.class);
                                if (!p.getWorkoutExercise().getName().equals("A")) {
                                    p.setChecked(false);
                                    listPractices.add(p);
                                    DatabaseReference mr1 = ref.child(("listPractice")).child(date).child(p.getWorkoutExercise().getName());
                                    mr1.setValue(p);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            tv_excer.setText(totalExcer + "");
                            tv_excer_2.setText(totalExcer + " Calories");
                            totalRemain = goal - totalFood + totalExcer;
                            tv_remain.setText(totalRemain + "");
                            DatabaseReference mref = ref.child(("Statistic")).child(date).child("totalRemain");
                            mref.setValue(totalRemain);
                            DatabaseReference mre = ref.child(("Statistic")).child(date).child("totalExercise");
                            mre.setValue(totalExcer);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTotal();
                updateUI();
                listPractices.clear();
                time = methodUtils.UpDownDay(date, -1);
                if (time.equals(methodUtils.getTimeNow())) {
                    tv_date.setText("Today");
                } else {
                    tv_date.setText(time);
                }
                date = time;
                adapter.setDate(time);
                adapter.notifyDataSetChanged();
                ref.child("Statistic").child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            Statistic s = dataSnapshot.getValue(Statistic.class);
                            goal = s.getTotalGoal();
                            tv_goal.setText(goal + "");
                            totalBreak = s.getTotalBreakfast();
                            totalDinner = s.getTotalDinner();
                            totalLunch = s.getTotalLunch();
                            totalSnak = s.getTotalSnack();
                            totalFood = totalBreak + totalDinner + totalLunch + totalSnak;
                            tv_breakfast.setText(totalBreak + "");
                            tv_lunch.setText(totalLunch + "");
                            tv_snack.setText(totalSnak + "");
                            tv_dinner.setText(totalDinner + "");
                            tv_food.setText(totalFood + "");
                            tv_food_2.setText(totalFood + " Calories");
                            totalRemain = goal - totalFood + totalExcer;
                            tv_remain.setText(totalRemain + "");
//                            tv_goal.setText(goal + "");
                            ref.child("listPractice").child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    listPractices.clear();
                                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                                        Practice p = i.getValue(Practice.class);
                                        if (!p.getWorkoutExercise().getName().equals("A")) {
                                            listPractices.add(p);
                                            if (p.isChecked()) {
                                                totalExcer = totalExcer + p.getWorkoutExercise().getKalo();
                                            }
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    tv_excer.setText(totalExcer + "");
                                    tv_excer_2.setText(totalExcer + " Calories");
                                    totalRemain = goal - totalFood + totalExcer;
                                    tv_remain.setText(totalRemain + "");
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            listPractices.clear();
                            adapter.notifyDataSetChanged();
                        }
                        Log.e("ahihi", dataSnapshot.toString());


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                time = methodUtils.UpDownDay(date, 1);
                if (methodUtils.compareDate(time) == 2) {
                    Toast.makeText(getActivity(), "Ngày " + date + " chưa đến, Bạn không thể xem Statistic", Toast.LENGTH_LONG).show();
                    return;
                }
                if (time.equals(methodUtils.getTimeNow())) {
                    tv_date.setText("Today");
                } else {
                    tv_date.setText(time);
                }

                date = time;
                initTotal();
                updateUI();
                listPractices.clear();
                adapter.setDate(time);

                adapter.notifyDataSetChanged();
                ref.child("Statistic").child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            Statistic s = dataSnapshot.getValue(Statistic.class);
                            goal = s.getTotalGoal();
                            tv_goal.setText(goal + "");
                            totalBreak = s.getTotalBreakfast();
                            totalDinner = s.getTotalDinner();
                            totalLunch = s.getTotalLunch();
                            totalSnak = s.getTotalSnack();
                            totalFood = totalBreak + totalDinner + totalLunch + totalSnak;
                            tv_breakfast.setText(totalBreak + "");
                            tv_lunch.setText(totalLunch + "");
                            tv_snack.setText(totalSnak + "");
                            tv_dinner.setText(totalDinner + "");
                            tv_food.setText(totalFood + "");
                            tv_food_2.setText(totalFood + " Calories");
                            totalRemain = goal - totalFood + totalExcer;
                            tv_remain.setText(totalRemain + "");
//                            tv_goal.setText(goal + "");
                            ref.child("listPractice").child(time).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    listPractices.clear();
                                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                                        Practice p = i.getValue(Practice.class);
                                        if (!p.getWorkoutExercise().getName().equals("A")) {
                                            listPractices.add(p);
                                            if (p.isChecked()) {
                                                totalExcer = totalExcer + p.getWorkoutExercise().getKalo();
                                            }
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    tv_excer.setText(totalExcer + "");
                                    tv_excer_2.setText(totalExcer + " Calories");
                                    totalRemain = goal - totalFood + totalExcer;
                                    tv_remain.setText(totalRemain + "");
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            listPractices.clear();
                            adapter.notifyDataSetChanged();
                        }
                        Log.e("ahihi", dataSnapshot.toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

//
//        totalRemain = goal - totalFood + totalExcer;
//        tv_remain.setText(totalRemain + "");

    }

    private void updateUI() {
        tv_goal.setText(goal + "");
        tv_food.setText(totalFood + "");
        tv_breakfast.setText(totalBreak + "");
        tv_dinner.setText(totalDinner + "");
        tv_lunch.setText(totalLunch + "");
        tv_snack.setText(totalSnak + "");
        tv_excer.setText(totalExcer + "");
        tv_excer_2.setText(totalExcer + " Calories");
        totalRemain = goal - totalFood + totalExcer;
        tv_remain.setText(totalRemain + "");
        tv_food_2.setText(totalFood + " Calories");
    }


    private void init() {

        tv_goal = (TextView) getView().findViewById(R.id.tv_plan_goal);
        tv_food = (TextView) getView().findViewById(R.id.tv_plan_food);
        tv_food_2 = (TextView) getView().findViewById(R.id.tv_food_2_statistic);
        tv_excer = (TextView) getView().findViewById(R.id.tv_plan_excer);
        tv_breakfast = (TextView) getView().findViewById(R.id.tv_plan_break);
        tv_dinner = (TextView) getView().findViewById(R.id.tv_plan_dinner);
        tv_lunch = (TextView) getView().findViewById(R.id.tv_plan_lunch);
        tv_snack = (TextView) getView().findViewById(R.id.tv_plan_snak);
        tv_excer_2 = (TextView) getView().findViewById(R.id.tv_plan_excer_2);
        tv_remain = (TextView) getView().findViewById(R.id.tv_plan_remain);
        tv_date = (TextView) getView().findViewById(R.id.tv_plan_date);
        iv_back = (ImageView) getView().findViewById(R.id.iv_plan_back_left);
        iv_next = (ImageView) getView().findViewById(R.id.iv_plan_back_right);
        lvPractice = (ListView) getView().findViewById(R.id.lv_statistic_list_exercise);
    }

    private void initTotal() {
        totalBreak = 0;
        totalExcer = 0;
        totalFood = 0;
        totalRemain = 0;
        goal = 0;
        totalLunch = 0;
        totalRemain = 0;
        totalDinner = 0;
        totalSnak = 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layout_main, fragment);
        ft.commit();
    }


    @Override
    public void calculate(int i, boolean value, String cv) {
        if (cv.equals("checked")) {
            listPractices.get(i).setChecked(value);
            if (value == true) {
                totalExcer = totalExcer + (float) listPractices.get(i).getWorkoutExercise().getKalo();
            } else {
                totalExcer = totalExcer - (float) listPractices.get(i).getWorkoutExercise().getKalo();
            }
            tv_excer.setText(totalExcer + "");
            tv_excer_2.setText(totalExcer + "Calories");
            totalRemain = goal - totalFood + totalExcer;
            tv_remain.setText(totalRemain + "");
            ref.child(("Statistic")).child(date).child("totalExercise").setValue(totalExcer);
            ref.child(("Statistic")).child(date).child("totalRemain").setValue(totalRemain);
            adapter.notifyDataSetChanged();

        } else {
            if (listPractices.get(i).isChecked()) {
                totalExcer = totalExcer - (float) listPractices.get(i).getWorkoutExercise().getKalo();
            }
            if (totalExcer < 0) {
                totalExcer = 0;
            }

            tv_excer.setText(totalExcer + "");
            tv_excer_2.setText(totalExcer + "Calories");
            totalRemain = goal - totalFood + totalExcer;
            tv_remain.setText(totalRemain + "");
            ref.child(("Statistic")).child(date).child("totalExercise").setValue(totalExcer);
            ref.child(("Statistic")).child(date).child("totalRemain").setValue(totalRemain);
            ref.child("listPractice").child(date).child(listPractices.get(i).getWorkoutExercise().getName()).setValue(null);
            listPractices.remove(i);
            adapter.notifyDataSetChanged();


        }


    }
}
