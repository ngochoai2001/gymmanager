package com.asignment.gymmanager.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;

import com.asignment.gymmanager.activity.MainActivity;
import com.asignment.gymmanager.R;
import com.asignment.gymmanager.adapter.ListWorkoutAdapter;
import com.asignment.gymmanager.model.Workout;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView gvWorkout;
    private ArrayList<Workout> listWorkouts = new ArrayList<>();
    private ListWorkoutAdapter adapter;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    public static WorkoutFragment newInstance() {
        WorkoutFragment fragment = new WorkoutFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        ((MainActivity) getActivity()).updateActionbar(false, false);
        MainActivity.stateWorkout = ConstantUtils.FRAGMENT_WORKOUT;
        ((MainActivity) getActivity()).updateTitle(MainActivity.page, MainActivity.stateWorkout);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_workout, container, false);
        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gvWorkout = (GridView) getView().findViewById(R.id.gv_workout);
        ref.child("Workout").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listWorkouts.clear();
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    Workout w = i.getValue(Workout.class);
                    listWorkouts.add(w);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new ListWorkoutAdapter(getActivity(), listWorkouts);
        gvWorkout.setAdapter(adapter);
        gvWorkout.setOnItemClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        WorkoutExerciseFragment fragment = new WorkoutExerciseFragment().newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("key", i + "");
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(this);
        ft.replace(R.id.layout_workout, fragment, ConstantUtils.FRAGMENT_TAG_WORKOUT_EXERCISE);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();
    }

}
