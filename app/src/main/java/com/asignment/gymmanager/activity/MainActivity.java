package com.asignment.gymmanager.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.asignment.gymmanager.R;
import com.asignment.gymmanager.fragment.ExerciseFragment;
import com.asignment.gymmanager.fragment.HomeFragment;
import com.asignment.gymmanager.fragment.MealFragment;
import com.asignment.gymmanager.fragment.ProfileFragment;
import com.asignment.gymmanager.fragment.StatisticFragment;
import com.asignment.gymmanager.fragment.WorkoutFragment;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.asignment.gymmanager.utils.OnAddPressedListener;
import com.asignment.gymmanager.utils.OnBackPressedListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        RadioGroup.OnCheckedChangeListener {
    public static int page = 1;
    public static int stateMain = 0;
    public static int stateWorkout = 0;
    public static int stateMeal = 0;
    public static int stateExercise = 0;
    public static int stateStatistic = 0;
    protected OnBackPressedListener onBackPressedListener;
    protected OnAddPressedListener onAddPressedListener;
    private RadioGroup bottomBar;
    private RadioButton rbHome, rbWorkout, rbMeal, rbExercise, rbStatistic;
    private TextView tvTitleActionbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ImageView ivBack, ivAdd;
    private FrameLayout layoutMain, layoutWorkout, layoutMeal, layoutExercise, layoutStatistic;
    private TextView tvAccName, tvAccEmail;
    private CircleImageView ivAccImage;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        FirebaseUser user = mAuth.getCurrentUser();
        View v = navigationView.getHeaderView(0);
        tvAccName =  v.findViewById(R.id.tv_nav_profile_name);
        tvAccEmail = v.findViewById(R.id.tv_nav_profile_email);
        ivAccImage = v.findViewById(R.id.iv_nav_profile_image);
        tvAccName.setText(user.getDisplayName());
        tvAccEmail.setText(user.getEmail());
        Glide.with(getApplicationContext()).load(user.getPhotoUrl())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivAccImage);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customActionbar = LayoutInflater.from(this).inflate(R.layout.action_bar_layout, null);
        getSupportActionBar().setCustomView(customActionbar);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onAddPressedListener != null) {
                    onAddPressedListener.doAdd();
                }
            }
        });
        bottomBar.setOnCheckedChangeListener(this);
        Fragment fragment1 = HomeFragment.newInstance();
        Fragment fragment2 = WorkoutFragment.newInstance();
        Fragment fragment3 = MealFragment.newInstance();
        Fragment fragment4 = ExerciseFragment.newInstance();
        Fragment fragment5 = StatisticFragment.newInstance();
        replaceFragment(fragment1, ConstantUtils.FRAGMENT_TAG_HOME, R.id.layout_main);
        replaceFragment(fragment2, ConstantUtils.FRAGMENT_TAG_WORKOUT, R.id.layout_workout);
        replaceFragment(fragment3, ConstantUtils.FRAGMENT_TAG_MEAL, R.id.layout_meal);
        replaceFragment(fragment4, ConstantUtils.FRAGMENT_TAG_EXERCISE, R.id.layout_exercise);
        replaceFragment(fragment5, ConstantUtils.FRAGMENT_TAG_STATISTIC, R.id.layout_statistic);
        navigationView.setCheckedItem(R.id.nav_home);

    }
    public void replaceFragment(Fragment fragment, String TAG, int id) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(fragment.getClass().getName());
        ft.replace(id, fragment, TAG);
        ft.commit();
    }

    private void initView() {
        tvTitleActionbar = findViewById(R.id.tv_title_actionbar);
        ivAdd =findViewById(R.id.iv_add);
        ivBack = findViewById(R.id.iv_actionbar_back);
        bottomBar = findViewById(R.id.bottom_bar);
        rbHome =  findViewById(R.id.rb_home);
        rbWorkout =  findViewById(R.id.rb_workout);
        rbMeal = findViewById(R.id.rb_meal);
        rbExercise = findViewById(R.id.rb_exercise);
        rbStatistic = findViewById(R.id.rb_statistic);
        layoutMain =  findViewById(R.id.layout_main);
        layoutWorkout = findViewById(R.id.layout_workout);
        layoutMeal = findViewById(R.id.layout_meal);
        layoutExercise = findViewById(R.id.layout_exercise);
        layoutStatistic = findViewById(R.id.layout_statistic);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_home: {
                if (page == 1) break;
                page = 1;
                updateFragment(page);
                updateTitle(page, stateMain);
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            }
            case R.id.rb_workout: {
                if (page == 2) break;
                page = 2;
                updateFragment(page);
                updateTitle(page, stateWorkout);
                navigationView.setCheckedItem(R.id.nav_workout);
                break;
            }
            case R.id.rb_meal: {
                if (page == 3) break;
                page = 3;
                updateFragment(page);
                updateTitle(page, stateMeal);
                navigationView.setCheckedItem(R.id.nav_meal);
                break;
            }
            case R.id.rb_exercise: {
                if (page == 4) break;
                page = 4;
                updateFragment(page);
                updateTitle(page, stateExercise);
                navigationView.setCheckedItem(R.id.nav_exercise);
                break;
            }
            case R.id.rb_statistic: {
                if (page == 5) break;
                page = 5;
                stateStatistic = ConstantUtils.FRAGMENT_STATISTIC;
                updateFragment(page);
                updateTitle(page, stateStatistic);
                Fragment fragment = new StatisticFragment().newInstance();
                replaceFragment(fragment, ConstantUtils.FRAGMENT_TAG_STATISTIC, R.id.layout_statistic);
                navigationView.setCheckedItem(R.id.nav_statistic);
                break;
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_profile:
                bottomBar.clearCheck();
                page = 5;
                updateFragment(page);
                stateStatistic = ConstantUtils.FRAGMENT_PROFILE;
                updateTitle(page, stateStatistic);
                updateActionbar(false, false);
                Fragment fragment = new ProfileFragment().newInstance();
                replaceFragment(fragment, ConstantUtils.FRAGMENT_TAG_PROFILE, R.id.layout_statistic);
                break;
            case R.id.nav_home:
                page = 1;
                rbHome.setChecked(true);
                updateFragment(page);
                updateTitle(page, stateMain);
                break;
            case R.id.nav_workout:
                page = 2;
                updateFragment(page);
                rbWorkout.setChecked(true);
                updateTitle(page, stateWorkout);
                break;
            case  R.id.nav_meal:
                page = 3;
                updateFragment(page);
                rbMeal.setChecked(true);
                updateTitle(page, stateMeal);
                break;

            case R.id.nav_exercise:
                page = 4;
                updateFragment(page);
                rbExercise.setChecked(true);
                updateTitle(page, stateExercise);
                break;
            case R.id.nav_statistic:
                page = 5;
                updateFragment(page);
                rbStatistic.setChecked(true);
                updateTitle(page, stateStatistic);
                break;
            case  R.id.nav_logout:
                signOut();
                this.finish();
            case R.id.nav_group:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Gym Manager");
                builder.setMessage("Trần Thị Ngọc Hoài");
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.nav_map:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        }



    public void updateFragment(int page) {
        switch (page) {
            case 1: {
                layoutMain.setVisibility(View.VISIBLE);
                layoutWorkout.setVisibility(View.INVISIBLE);
                layoutMeal.setVisibility(View.INVISIBLE);
                layoutExercise.setVisibility(View.INVISIBLE);
                layoutStatistic.setVisibility(View.INVISIBLE);
                break;
            }
            case 2: {
                layoutMain.setVisibility(View.INVISIBLE);
                layoutWorkout.setVisibility(View.VISIBLE);
                layoutMeal.setVisibility(View.INVISIBLE);
                layoutExercise.setVisibility(View.INVISIBLE);
                layoutStatistic.setVisibility(View.INVISIBLE);
                break;
            }
            case 3: {
                layoutMain.setVisibility(View.INVISIBLE);
                layoutWorkout.setVisibility(View.INVISIBLE);
                layoutMeal.setVisibility(View.VISIBLE);
                layoutExercise.setVisibility(View.INVISIBLE);
                layoutStatistic.setVisibility(View.INVISIBLE);
                break;
            }
            case 4: {
                layoutMain.setVisibility(View.INVISIBLE);
                layoutWorkout.setVisibility(View.INVISIBLE);
                layoutMeal.setVisibility(View.INVISIBLE);
                layoutExercise.setVisibility(View.VISIBLE);
                layoutStatistic.setVisibility(View.INVISIBLE);
                break;
            }
            case 5: {
                layoutMain.setVisibility(View.INVISIBLE);
                layoutWorkout.setVisibility(View.INVISIBLE);
                layoutMeal.setVisibility(View.INVISIBLE);
                layoutExercise.setVisibility(View.INVISIBLE);
                layoutStatistic.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    public void updateTitle(int page, int state) {
        switch (page) {
            case 1: {
                switch (stateMain) {
                    case ConstantUtils.FRAGMENT_HOME: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_HOME);
                        updateActionbar(false, false);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_BLOG: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_BLOG);
                        updateActionbar(true, false);
                        break;
                    }
                    case 0: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_HOME);
                        updateActionbar(false, false);
                        break;
                    }
                }
                break;
            }
            case 2: {
                switch (stateWorkout) {
                    case ConstantUtils.FRAGMENT_WORKOUT: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_WORKOUT);
                        updateActionbar(false, false);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_WORKOUT_EXERCISE: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_WORKOUT);
                        updateActionbar(true, false);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_EXERCISE_DETAIL: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_EXERCISE_DETAIL);
                        updateActionbar(true, false);
                        break;
                    }
                    case 0: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_WORKOUT);
                        updateActionbar(false, false);
                        break;
                    }
                }
                break;
            }
            case 3: {
                switch (stateMeal) {
                    case ConstantUtils.FRAGMENT_MEAL: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_MEAL);
                        updateActionbar(false, false);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_LIST_FOOD: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_FOOD);
                        updateActionbar(true, true);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_FOOD_DETAIL: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_FOOD_DETAIL);
                        updateActionbar(true, false);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_ADD_FOOD: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_FOOD);
                        updateActionbar(true, true);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_MEAL_BREAKFAST: {
                        tvTitleActionbar.setText(ConstantUtils.Breakfast);
                        updateActionbar(true, true);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_MEAL_LUNCH: {
                        tvTitleActionbar.setText(ConstantUtils.Lunch);
                        updateActionbar(true, true);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_MEAL_DINNER: {
                        tvTitleActionbar.setText(ConstantUtils.Dinner);
                        updateActionbar(true, true);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_MEAL_SNACK: {
                        tvTitleActionbar.setText(ConstantUtils.Snack);
                        updateActionbar(true, true);
                        break;
                    }
                    case 0: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_MEAL);
                        updateActionbar(false, false);
                        break;
                    }
                }
                break;
            }
            case 4: {
                switch (stateExercise) {
                    case ConstantUtils.FRAGMENT_EXERCISE: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_EXERCISE);
                        updateActionbar(false, false);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_LIST_EXERCISE: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_EXERCISE);
                        updateActionbar(true, false);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_EXERCISE_DETAIL: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_EXERCISE_DETAIL);
                        updateActionbar(true, false);
                        break;
                    }
                    case 0: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_EXERCISE);
                        updateActionbar(false, false);
                        break;
                    }
                }
                break;
            }
            case 5: {
                switch (state) {
                    case ConstantUtils.FRAGMENT_PROFILE: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_PROFILE);
                        updateActionbar(false, false);
                        break;
                    }
                    case ConstantUtils.FRAGMENT_STATISTIC: {
                        tvTitleActionbar.setText(ConstantUtils.TITLE_STATISTIC);
                        updateActionbar(false, false);
                        break;
                    }
                }
                break;
            }
        }
    }

    public void updateActionbar(boolean isShowBack, boolean isShowAdd) {
        if (isShowBack) {
            toggle.setDrawerIndicatorEnabled(false);
            ivBack.setVisibility(View.VISIBLE);
        } else {
            toggle.setDrawerIndicatorEnabled(true);
            ivBack.setVisibility(View.GONE);
        }
        if (isShowAdd) {
            ivAdd.setVisibility(View.VISIBLE);
        } else {
            ivAdd.setVisibility(View.INVISIBLE);
        }

    }
    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    public void setOnAddPressedListener(OnAddPressedListener onAddPressedListener) {
        this.onAddPressedListener = onAddPressedListener;
    }
    private void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }
}