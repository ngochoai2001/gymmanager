package com.asignment.gymmanager.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asignment.gymmanager.R;
import com.asignment.gymmanager.model.Exercise;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ListExerciseAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Exercise> listExercise = new ArrayList<>();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference sref = storage.getReference();
    private String part;

    public ListExerciseAdapter() {
    }

    public ListExerciseAdapter(Activity activity, ArrayList<Exercise> listExercise, String part) {
        this.activity = activity;
        this.listExercise = listExercise;
        this.part = part;
    }

    @Override
    public int getCount() {
        return listExercise.size();
    }

    @Override
    public Object getItem(int position) {
        return listExercise.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Viewholder viewholder;
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.item_list_exercise, parent, false);
            viewholder = new Viewholder();
            viewholder.iv_exercise = (ImageView) view.findViewById(R.id.iv_exercise_image);
            viewholder.tv_exercise_title = (TextView) view.findViewById(R.id.tv_exercise_title);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }

        viewholder.tv_exercise_title.setText(listExercise.get(position).getName());
        StorageReference mref = sref.child("exercise/" + listExercise.get(position).getImageUrl().get(0));
        Glide.with(activity)
//                .using(new FirebaseImageLoader())
                .load(mref)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewholder.iv_exercise);

        final Exercise ex = listExercise.get(position);
//        viewholder.iv_exercise.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("position", position);
//                bundle.putString("part", part);
//                Fragment fragment = new ExerciseDetailFragment().newInstance();
//                fragment.setArguments(bundle);
//                replaceFragment(fragment);
//            }
//        });
//
//        viewholder.tv_exercise_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putInt("position", position);
//                bundle.putString("part", part);
//                Fragment fragment = new ExerciseDetailFragment().newInstance();
//                fragment.setArguments(bundle);
//                replaceFragment(fragment);
//            }
//        });
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(fragment.getClass().getName());
        ft.add(R.id.layout_exercise, fragment, ConstantUtils.FRAGMENT_TAG_LIST_EXERCISE);
        ft.commit();
    }

    private class Viewholder {
        ImageView iv_exercise;
        TextView tv_exercise_title;
    }
}
