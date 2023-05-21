package com.asignment.gymmanager.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.asignment.gymmanager.activity.MainActivity;
import com.asignment.gymmanager.R;
import com.asignment.gymmanager.model.Exercise;
import com.asignment.gymmanager.model.Practice;
import com.asignment.gymmanager.model.WorkoutExercise;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.asignment.gymmanager.utils.MethodUtils;
import com.asignment.gymmanager.utils.OnBackPressedListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ExerciseDetailFragment extends Fragment implements OnBackPressedListener {

    private Button btAdd;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference sref = storage.getReference();
    private Exercise exercise = new Exercise();
    private TextView title, instruction, calo, tv_totalcalo, set, rep;
    private ImageView image, set_plus, set_sub, rep_plus, rep_sub;
    private int position;
    private int setno = 1, repno = 1;
    private String part = "", reference = "", key = "";

    public static ExerciseDetailFragment newInstance() {
        ExerciseDetailFragment fragment = new ExerciseDetailFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        MainActivity.stateExercise = ConstantUtils.FRAGMENT_EXERCISE_DETAIL;
        ((MainActivity) getActivity()).updateTitle(MainActivity.page, MainActivity.stateExercise);
//        ((MainActivity) getActivity()).updateActionbar(true, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_exercise_detail, container, false);
        title = (TextView) viewGroup.findViewById(R.id.tv_exercise_title);
        calo = (TextView) viewGroup.findViewById(R.id.tv_calo_burn);
        instruction = (TextView) viewGroup.findViewById(R.id.tv_exercise_instruction);
        image = (ImageView) viewGroup.findViewById(R.id.iv_exercise_detail_image);
        btAdd = (Button) viewGroup.findViewById(R.id.bt_add_exercise);
        return viewGroup;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (reference != null) {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReferenceFromUrl(reference);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    exercise = dataSnapshot.getValue(Exercise.class);
                    title.setText(exercise.getName());
                    calo.setText(exercise.getCalo() + "");
                    instruction.setText(exercise.getContent());
                    StorageReference ref = sref.child("exercise/" + exercise.getImageUrl().get(1));
                    Glide.with(getActivity())
//                            .using(new FirebaseImageLoader())
                            .load(ref)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(image);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return;
        }

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
        dref.child("Exercise").child(part).child(position + "").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exercise = dataSnapshot.getValue(Exercise.class);
                title.setText(exercise.getName());
                calo.setText(exercise.getCalo() + "");
                instruction.setText(exercise.getContent());
                StorageReference mref = sref.child("exercise/" + exercise.getImageUrl().get(1));
                Glide.with(getActivity())
//                        .using(new FirebaseImageLoader())
                        .load(mref)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
                builder.setView(view);
                builder.setTitle("Add to daily practice");

                set_plus = (ImageView) view.findViewById(R.id.iv_set_plus);
                set_sub = (ImageView) view.findViewById(R.id.iv_set_sub);
                rep_plus = (ImageView) view.findViewById(R.id.iv_rep_plus);
                rep_sub = (ImageView) view.findViewById(R.id.iv_rep_sub);
                set = (TextView) view.findViewById(R.id.tv_setno);
                rep = (TextView) view.findViewById(R.id.tv_repno);
                tv_totalcalo = (TextView) view.findViewById(R.id.tv_total_calo_burn);

                tv_totalcalo.setText((exercise.getCalo() * Integer.parseInt(set.getText().toString()) * Integer.parseInt(rep.getText().toString())) + "");

                set_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setno++;
                        set.setText(setno + "");
                        tv_totalcalo.setText((exercise.getCalo() * Integer.parseInt(set.getText().toString()) * Integer.parseInt(rep.getText().toString())) + "");

                    }
                });
                set_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (setno > 1) {
                            setno--;
                        } else {
                            setno = 1;
                            Toast.makeText(getActivity(), "Minimum number of set is ONE", Toast.LENGTH_SHORT).show();
                        }
                        set.setText(setno + "");
                        tv_totalcalo.setText((exercise.getCalo() * Integer.parseInt(set.getText().toString()) * Integer.parseInt(rep.getText().toString())) + "");

                    }
                });
                rep_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        repno++;
                        rep.setText(repno + "");
                        tv_totalcalo.setText((exercise.getCalo() * Integer.parseInt(set.getText().toString()) * Integer.parseInt(rep.getText().toString())) + "");

                    }
                });
                rep_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (repno > 1) {
                            repno--;
                        } else {
                            repno = 1;
                        }
                        rep.setText(repno + "");
                        tv_totalcalo.setText((exercise.getCalo() * Integer.parseInt(set.getText().toString()) * Integer.parseInt(rep.getText().toString())) + "");
                        Toast.makeText(getActivity(), "Minimum number of rep is ONE", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MethodUtils methodUtils = new MethodUtils();
                                final String time = methodUtils.getTimeNow();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                DatabaseReference mref = ref.child("listPractice").child(time).child(exercise.getName());
                                WorkoutExercise we = new WorkoutExercise("we_btl", exercise.getName(),
                                        Float.parseFloat(tv_totalcalo.getText().toString()), setno, "", repno,
                                        exercise.getContent(), false, "");
                                Practice p = new Practice(we, false, "");
                                mref.setValue(p);
                                Toast.makeText(getActivity(), "Added successfully. Check Statistic.", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            reference = bundle.getString("exerciseRef");
            key = bundle.getString("key");
            if (reference == null) {
                part = bundle.getString("part");
                position = bundle.getInt("position");
            }
        }
    }

    @Override
    public void doBack() {
        if (MainActivity.page == 2) {
            Fragment fragment = new WorkoutExerciseFragment().newInstance();
            Bundle b = new Bundle();
            b.putString("key", key);
            fragment.setArguments(b);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.layout_workout, fragment, ConstantUtils.FRAGMENT_TAG_WORKOUT_EXERCISE).commit();
        } else if (MainActivity.page == 4) {
            Fragment fragment = new ListExerciseFragment().newInstance();
            Bundle b = new Bundle();
            b.putString("exercise", part);
            fragment.setArguments(b);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.layout_exercise, fragment).commit();
        }
    }

}
