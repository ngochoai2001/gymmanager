package com.asignment.gymmanager.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.asignment.gymmanager.activity.MainActivity;
import com.asignment.gymmanager.R;
import com.asignment.gymmanager.utils.ConstantUtils;

import java.util.ArrayList;
import java.util.List;


public class ExerciseFragment extends Fragment implements View.OnClickListener {

    List<String> listWorkout = new ArrayList<>();
    private LinearLayout layoutBack, layoutAbs, layoutBicep, layoutTricep, layoutChest, layoutShoulder, layoutLeg;

    public static ExerciseFragment newInstance() {

        ExerciseFragment fragment = new ExerciseFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        MainActivity.stateExercise = ConstantUtils.FRAGMENT_EXERCISE;
        ((MainActivity) getActivity()).updateTitle(MainActivity.page, MainActivity.stateExercise);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_exercise, container, false);
        layoutAbs = (LinearLayout) view.findViewById(R.id.layout_abdominals);
        layoutAbs.setOnClickListener(this);
        layoutBack = (LinearLayout) view.findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);
        layoutBicep = (LinearLayout) view.findViewById(R.id.layout_biceps);
        layoutBicep.setOnClickListener(this);
        layoutTricep = (LinearLayout) view.findViewById(R.id.layout_triceps);
        layoutTricep.setOnClickListener(this);
        layoutChest = (LinearLayout) view.findViewById(R.id.layout_chest);
        layoutChest.setOnClickListener(this);
        layoutShoulder = (LinearLayout) view.findViewById(R.id.layout_shoulders);
        layoutShoulder.setOnClickListener(this);
        layoutLeg = (LinearLayout) view.findViewById(R.id.layout_legs);
        layoutLeg.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        Bundle b = new Bundle();
        switch (view.getId()) {
            case R.id.layout_back: {
                b.putString("exercise", ConstantUtils.EXERCISE_BACK);
                Fragment fragment = new ListExerciseFragment().newInstance();
                fragment.setArguments(b);
                replaceFragment(fragment);
//                ((MainActivity) getActivity()).replaceFragment(fragment);
                break;
            }
            case R.id.layout_abdominals: {
                b.putString("exercise", ConstantUtils.EXERCISE_ABS);
                Fragment fragment = new ListExerciseFragment().newInstance();
                fragment.setArguments(b);
                replaceFragment(fragment);
                break;
            }
            case R.id.layout_biceps: {
                b.putString("exercise", ConstantUtils.EXERCISE_BICEPS);
                Fragment fragment = new ListExerciseFragment().newInstance();
                fragment.setArguments(b);
                replaceFragment(fragment);
                break;
            }
            case R.id.layout_triceps: {
                b.putString("exercise", ConstantUtils.EXERCISE_TRICEPS);
                Fragment fragment = new ListExerciseFragment().newInstance();
                fragment.setArguments(b);
                replaceFragment(fragment);
                break;
            }
            case R.id.layout_chest: {
                b.putString("exercise", ConstantUtils.EXERCISE_CHEST);
                Fragment fragment = new ListExerciseFragment().newInstance();
                fragment.setArguments(b);
                replaceFragment(fragment);
                break;
            }
            case R.id.layout_shoulders: {
                b.putString("exercise", ConstantUtils.EXERCISE_SHOULDERS);
                Fragment fragment = new ListExerciseFragment().newInstance();
                fragment.setArguments(b);
                replaceFragment(fragment);
                break;
            }
            case R.id.layout_legs: {
                b.putString("exercise", ConstantUtils.EXERCISE_LEGS);
                Fragment fragment = new ListExerciseFragment().newInstance();
                fragment.setArguments(b);
                replaceFragment(fragment);
                break;
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layout_exercise, fragment, ConstantUtils.FRAGMENT_TAG_LIST_EXERCISE);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
