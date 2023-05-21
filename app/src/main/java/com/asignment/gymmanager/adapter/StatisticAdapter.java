package com.asignment.gymmanager.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asignment.gymmanager.R;
import com.asignment.gymmanager.model.Practice;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.asignment.gymmanager.utils.MethodUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class StatisticAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Practice> listPractices = new ArrayList<>();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private String date = "";
    private onCheckedChangeListener mCallback;


    public StatisticAdapter(Activity activity, ArrayList<Practice> listPractices, onCheckedChangeListener callback) {
        this.activity = activity;
        this.listPractices = listPractices;
        this.mCallback = callback;
    }

    public StatisticAdapter(Activity activity, ArrayList<Practice> listPractices, String date, onCheckedChangeListener callback) {
        this.activity = activity;
        this.listPractices = listPractices;
        this.date = date;
        this.mCallback = callback;
    }

    @Override
    public int getCount() {
        return listPractices.size();
    }

    @Override
    public Object getItem(int position) {
        return listPractices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public View getView(final int i, View view, ViewGroup parent) {

        final Viewholder viewholder;
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.item_statistic, parent, false);
            viewholder = new Viewholder();
            viewholder.tvName =  view.findViewById(R.id.tv_statistic_name_excer);
            viewholder.tvNumber =  view.findViewById(R.id.tv_statistic_number_calo);
            viewholder.checkBox =  view.findViewById(R.id.checkbox_statistic);
            viewholder.iv_delete =  view.findViewById(R.id.iv_delete_statistic);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
            }
        if (!listPractices.get(i).getWorkoutExercise().getName().equals("A")) {
            viewholder.tvName.setText(listPractices.get(i).getWorkoutExercise().getName());
            viewholder.tvNumber.setText(listPractices.get(i).getWorkoutExercise().getKalo() + " " + ConstantUtils.unitCalo);
            viewholder.checkBox.setChecked(listPractices.get(i).isChecked());
            final boolean check = listPractices.get(i).isChecked();
            viewholder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MethodUtils methodUtils = new MethodUtils();
                    if (methodUtils.compareDate(date) == 1) {
                        Toast.makeText(activity, "Ngày " + date + " đã qua, Bạn không thể chỉnh sửa exercise", Toast.LENGTH_LONG).show();
                        viewholder.checkBox.setChecked(check);
                        notifyDataSetChanged();

                    } else {
                        DatabaseReference mref = ref.child("listPractice").child(date).child(listPractices.get(i).getWorkoutExercise().getName()).child("checked");
                        if (isChecked) {
                            listPractices.get(i).setChecked(true);
                            mref.setValue(true);
                            mCallback.calculate(i, true, "checked");
                        } else {
                            mref.setValue(false);
                            listPractices.get(i).setChecked(false);
                            mCallback.calculate(i, false, "checked");
                        }
                    }

                }
            });
            viewholder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MethodUtils methodUtils = new MethodUtils();
                    if (methodUtils.compareDate(date) == 1) {
                        Toast.makeText(activity, "Ngày " + date + " đã qua, Bạn không thể chỉnh sửa exercise", Toast.LENGTH_LONG).show();
                        viewholder.checkBox.setChecked(check);
                        notifyDataSetChanged();

                    } else {

                        mCallback.calculate(i, false, "delete");

                    }
                }
            });


            return view;
        }
        return view;

    }

    public interface onCheckedChangeListener {
        void calculate(int i, boolean value, String cv);
    }


    private class Viewholder {
        TextView tvName, tvNumber;
        ImageView iv_delete;
        CheckBox checkBox;
    }
}
