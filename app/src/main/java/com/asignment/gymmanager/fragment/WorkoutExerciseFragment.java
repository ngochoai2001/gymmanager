package com.asignment.gymmanager.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.Nullable;

import com.asignment.gymmanager.activity.MainActivity;
import com.asignment.gymmanager.R;
import com.asignment.gymmanager.adapter.ExpandableListworkoutAdapter;
import com.asignment.gymmanager.model.WorkoutExercise;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.asignment.gymmanager.utils.OnBackPressedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;



public class WorkoutExerciseFragment extends Fragment implements OnBackPressedListener {

    public static String key = "0";
    private ArrayList<WorkoutExercise> listWorkoutExercises = new ArrayList<>();
    private ExpandableListView exListview;
    private ExpandableListworkoutAdapter adapter;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private ArrayList<String> listHeader = new ArrayList<>();
    private HashMap<String, ArrayList<WorkoutExercise>> listdata = new HashMap<>();

    public static WorkoutExerciseFragment newInstance() {
        WorkoutExerciseFragment fragment = new WorkoutExerciseFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        MainActivity.stateWorkout = ConstantUtils.FRAGMENT_WORKOUT_EXERCISE;
        ((MainActivity) getActivity()).updateTitle(MainActivity.page, MainActivity.stateWorkout);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_workout_exercise, container, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        exListview = (ExpandableListView) getView().findViewById(R.id.exListview);
        ref.child("Workout").child(key).child("listWorkoutExercise").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("ahihi", dataSnapshot.toString());
                listWorkoutExercises.clear();
                listdata.clear();
                listHeader.clear();
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    WorkoutExercise w = i.getValue(WorkoutExercise.class);
                    listWorkoutExercises.add(w);
                    listHeader.add(w.getName());
                    ArrayList<WorkoutExercise> list = new ArrayList<WorkoutExercise>();
                    list.add(w);
                    listdata.put(w.getName(), list);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new ExpandableListworkoutAdapter(getActivity(), listHeader, listdata);
        exListview.setAdapter(adapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            key = bundle.getString("key");
        }
    }

    @Override
    public void doBack() {
        getActivity().getFragmentManager().beginTransaction().replace(R.id.layout_workout, new WorkoutFragment().newInstance()).commit();
    }
}
