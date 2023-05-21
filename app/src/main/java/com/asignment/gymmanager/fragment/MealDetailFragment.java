package com.asignment.gymmanager.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.asignment.gymmanager.activity.MainActivity;
import com.asignment.gymmanager.R;
import com.asignment.gymmanager.adapter.MealDetailAdapter;
import com.asignment.gymmanager.model.Food;
import com.asignment.gymmanager.model.LineItem;
import com.asignment.gymmanager.model.Meal;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.asignment.gymmanager.utils.MethodUtils;
import com.asignment.gymmanager.utils.OnAddPressedListener;
import com.asignment.gymmanager.utils.OnBackPressedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MealDetailFragment extends Fragment implements OnBackPressedListener, OnAddPressedListener {
    private Meal meal = new Meal();
    private MealDetailAdapter adapter;
    private ListView listview;
    private TextView tv_total, tv_name_day;
    private ImageView iv_back_left, iv_back_right;
    private String date = "";
    private String typeMeal = "";
    private String idFood = "";
    private String number = "";
    private boolean isSuccess = false;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    public static MealDetailFragment newInstance() {
        MealDetailFragment fragment = new MealDetailFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        ((MainActivity) getActivity()).setOnAddPressedListener(this);
        ((MainActivity) getActivity()).updateActionbar(true, true);
        switch (typeMeal) {
            case ConstantUtils.Breakfast: {
                MainActivity.stateMeal = ConstantUtils.FRAGMENT_MEAL_BREAKFAST;
                break;
            }
            case ConstantUtils.Lunch: {
                MainActivity.stateMeal = ConstantUtils.FRAGMENT_MEAL_LUNCH;
                break;
            }
            case ConstantUtils.Dinner: {
                MainActivity.stateMeal = ConstantUtils.FRAGMENT_MEAL_DINNER;
                break;
            }
            case ConstantUtils.Snack: {
                MainActivity.stateMeal = ConstantUtils.FRAGMENT_MEAL_SNACK;
                break;
            }
        }
        ((MainActivity) getActivity()).updateTitle(MainActivity.page, MainActivity.stateMeal);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_meal_detail, container, false);
        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        innit();
        final MethodUtils methodUtils = new MethodUtils();

        ref.child(typeMeal).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
//                    Meal m = i.getValue(Meal.class);
//                    if (m.getDate().equals(date)) {
                    if (i.getKey().equals(date)) {
                        Meal m = i.getValue(Meal.class);
                        meal = m;
                        adapter = new MealDetailAdapter(getActivity(), meal);
                        tv_total.setText(" " + meal.getTotalCalo() + " ");
                        listview.setAdapter(adapter);
                    }
                }
                if (!isSuccess && !idFood.equals("")) {
                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
                    mref.child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot i : dataSnapshot.getChildren()) {
                                if (i.getKey().equals(idFood)) {
                                    Food f = i.getValue(Food.class);
                                    LineItem l = new LineItem(f, Integer.parseInt(number));
                                    meal.addLineitem(l);
                                    ref.child(typeMeal).child(date).updateChildren(meal.toMap());
                                    ref.child("Statistic").child(date).child("totalFood").setValue(meal.getTotalCalo());
                                    if (typeMeal.equals(ConstantUtils.Breakfast)) {
                                        ref.child(("Statistic")).child(date).child("totalBreakfast").setValue(meal.getTotalCalo());
                                    }
                                    if (typeMeal.equals(ConstantUtils.Lunch)) {
                                        ref.child(("Statistic")).child(date).child("totalLunch").setValue(meal.getTotalCalo());
                                    }
                                    if (typeMeal.equals(ConstantUtils.Dinner)) {
                                        ref.child(("Statistic")).child(date).child("totalDinner").setValue(meal.getTotalCalo());
                                    }
                                    if (typeMeal.equals(ConstantUtils.Snack)) {
                                        ref.child(("Statistic")).child(date).child("totalSnack").setValue(meal.getTotalCalo());
                                    }
                                    isSuccess = true;
                                    break;
                                }
                            }
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
        String timeNow = methodUtils.getTimeNow();
        if (timeNow.equals(date)) {
            tv_name_day.setText("Today");
        } else {
            tv_name_day.setText(date);
        }

        iv_back_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                meal = new Meal();
                tv_total.setText("");
                String time = methodUtils.UpDownDay(date, -1);
                if (time.equals(methodUtils.getTimeNow())) {
                    tv_name_day.setText("Today");

                } else {
                    tv_name_day.setText(time);

                }
                date = time;
                ref.child(typeMeal).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot i : dataSnapshot.getChildren()) {
                            Meal m = i.getValue(Meal.class);
                            if (m.getDate().equals(date)) {
                                meal = m;
                                adapter = new MealDetailAdapter(getActivity(), meal);
                                tv_total.setText(" " + meal.getTotalCalo() + " ");
                                listview.setAdapter(adapter);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                adapter = new MealDetailAdapter(getActivity(), meal);
                tv_total.setText(" " + meal.getTotalCalo() + " ");
                listview.setAdapter(adapter);


            }
        });
        iv_back_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                meal = new Meal();
                tv_total.setText("");
                String time = methodUtils.UpDownDay(date, 1);
                if (time.equals(methodUtils.getTimeNow())) {
                    tv_name_day.setText("Today");

                } else {
                    tv_name_day.setText(time);

                }

                date = time;
                ref.child(typeMeal).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot i : dataSnapshot.getChildren()) {
                            Meal m = i.getValue(Meal.class);
                            if (m.getDate().equals(date)) {
                                meal = m;
                                adapter = new MealDetailAdapter(getActivity(), meal);
                                tv_total.setText(" " + meal.getTotalCalo() + " ");
                                listview.setAdapter(adapter);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                adapter = new MealDetailAdapter(getActivity(), meal);
                tv_total.setText(" " + meal.getTotalCalo() + " ");
                listview.setAdapter(adapter);


            }
        });


    }

    public void transToListFoodFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("typeMeal", typeMeal);
        bundle.putString("date", date);
        Fragment fragment = new ListFoodFragment().newInstance();
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            typeMeal = bundle.getString("typeMeal", "");
            number = bundle.getString("number", "");
            idFood = bundle.getString("idFood", "");
            date = bundle.getString("date", "");
            MethodUtils methodUtils = new MethodUtils();
            if (methodUtils.compareDate(date) == 1) {
                Toast.makeText(getActivity(), "Ngày " + date + " đã qua, Bạn không thể thêm food", Toast.LENGTH_LONG).show();
            } else {
                meal.setDate(date);
                meal.setId(date);
                meal.setType(typeMeal);
            }
        }
    }

    private void innit() {
        tv_name_day = (TextView) getView().findViewById(R.id.tv_name_day);
        tv_total = (TextView) getView().findViewById(R.id.tv_total_calo);
        iv_back_left = (ImageView) getView().findViewById(R.id.iv_back_day);
        iv_back_right = (ImageView) getView().findViewById(R.id.iv_next_day);
        listview = (ListView) getView().findViewById(R.id.lv_food_meat);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(fragment.getClass().getName());
        ft.replace(R.id.layout_meal, fragment, ConstantUtils.FRAGMENT_TAG_LIST_FOOD);
        ft.commit();
    }

    @Override
    public void doBack() {
        Fragment fragment = new MealFragment().newInstance();
        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.replace(R.id.layout_meal, fragment, ConstantUtils.FRAGMENT_TAG_MEAL);
        ft.commit();
    }

    @Override
    public void doAdd() {
        if (MainActivity.stateMeal == ConstantUtils.FRAGMENT_MEAL_BREAKFAST
                || MainActivity.stateMeal == ConstantUtils.FRAGMENT_MEAL_LUNCH
                || MainActivity.stateMeal == ConstantUtils.FRAGMENT_MEAL_DINNER
                || MainActivity.stateMeal == ConstantUtils.FRAGMENT_MEAL_SNACK) {
            transToListFoodFragment();
        }
    }


}
