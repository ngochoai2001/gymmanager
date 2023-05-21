package com.asignment.gymmanager.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asignment.gymmanager.R;
import com.asignment.gymmanager.fragment.ExerciseDetailFragment;
import com.asignment.gymmanager.fragment.WorkoutExerciseFragment;
import com.asignment.gymmanager.model.Practice;
import com.asignment.gymmanager.model.WorkoutExercise;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.asignment.gymmanager.utils.MethodUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;



public class ExpandableListworkoutAdapter extends BaseExpandableListAdapter {
    private Activity activity;
    private ArrayList<String> listHeader = new ArrayList<>();
    private HashMap<String, ArrayList<WorkoutExercise>> listData = new HashMap<>();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    public ExpandableListworkoutAdapter(Activity activity, ArrayList<String> listHeader, HashMap<String, ArrayList<WorkoutExercise>> listData) {
        this.activity = activity;
        this.listHeader = listHeader;
        this.listData = listData;
    }

    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listData.get(listHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listData.get(listHeader.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int i, final boolean b, View view, ViewGroup viewGroup) {
        GroupHolder holder;
        MethodUtils methodUtils = new MethodUtils();
        final String time = methodUtils.getTimeNow();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.item_expandable_listview_header, null);
            holder = new GroupHolder();
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_expandable_list_header);
            holder.ivDetail = (ImageView) view.findViewById(R.id.iv_expandable_list_detail);
            holder.cbComplete = (CheckBox) view.findViewById(R.id.cb_expandable_list_complete);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }
        if (listData.get(listHeader.get(i)).get(0).isChecked()) {
            holder.cbComplete.setChecked(true);
            // DatabaseReference mref = ref.child("Statistic").child(time).child("listPractice").child(listData.get(listHeader.get(i)).get(0).getName());
            DatabaseReference mref = ref.child("listPractice").child(time).child(listData.get(listHeader.get(i)).get(0).getName());
            Practice p = new Practice(listData.get(listHeader.get(i)).get(0), false, "");
            mref.setValue(p);
        } else holder.cbComplete.setChecked(false);
        holder.tvTitle.setText(listHeader.get(i));
        holder.ivDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("exerciseRef", listData.get(listHeader.get(i)).get(0).getExerciseRef());
                bundle.putString("key", WorkoutExerciseFragment.key);
                Fragment fragment = new ExerciseDetailFragment().newInstance();
                fragment.setArguments(bundle);
                replaceFragment(fragment);
                Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.cbComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    DatabaseReference mref = ref.child("listPractice").child(time).child(listData.get(listHeader.get(i)).get(0).getName());
                    listData.get(listHeader.get(i)).get(0).setChecked(true);
                    Practice p = new Practice(listData.get(listHeader.get(i)).get(0), false, "");
                    mref.setValue(p);
                } else {
                    DatabaseReference mref = ref.child("listPractice").child(time).child(listData.get(listHeader.get(i)).get(0).getName());
                    //DatabaseReference mref = ref.child("Statistic").child(time).child("listPractice").child(listData.get(listHeader.get(i)).get(0).getName());
                    listData.get(listHeader.get(i)).get(0).setChecked(false);
                    mref.setValue(null);
                }
            }
        });
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder holder;
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.item_expandable_listview_child, null);
            holder = new ChildHolder();
            holder.tvSet = (TextView) view.findViewById(R.id.tv_expandable_list_set);
            holder.tvQuantity = (TextView) view.findViewById(R.id.tv_expandable_list_quantity);
            holder.tvContent = (TextView) view.findViewById(R.id.tv_expandable_list_content);
            holder.tvKalo = (TextView) view.findViewById(R.id.tv_expandable_list_kalo);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
        holder.tvSet.setText("Set: " + listData.get(listHeader.get(i)).get(i1).getSet());
        holder.tvQuantity.setText(listData.get(listHeader.get(i)).get(i1).getQuantity() + " " + listData.get(listHeader.get(i)).get(i1).getUnit() + " each set");
        holder.tvContent.setText(listData.get(listHeader.get(i)).get(i1).getContent());
        holder.tvKalo.setText("kalo: " + listData.get(listHeader.get(i)).get(i1).getKalo());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(fragment.getClass().getName());
        ft.replace(R.id.layout_workout, fragment, ConstantUtils.FRAGMENT_TAG_WORKOUT_EXERCISE_DETAIL);
        ft.commit();
    }

    private class ChildHolder {
        TextView tvSet, tvQuantity, tvContent, tvKalo;
    }

    private class GroupHolder {
        TextView tvTitle;
        ImageView ivDetail;
        CheckBox cbComplete;
    }
}
