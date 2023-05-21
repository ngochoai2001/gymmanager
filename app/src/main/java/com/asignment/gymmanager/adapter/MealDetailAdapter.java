package com.asignment.gymmanager.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asignment.gymmanager.R;
import com.asignment.gymmanager.model.Food;
import com.asignment.gymmanager.model.Meal;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.asignment.gymmanager.utils.MethodUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class MealDetailAdapter extends BaseAdapter {
    private Activity activity;
    private Meal meal;
    private int number = 1;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference sref = storage.getReference();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());


    public MealDetailAdapter(Activity activity, Meal meal) {
        this.activity = activity;
        this.meal = meal;
    }

    @Override
    public int getCount() {
        return meal.getItems().size();
    }

    @Override
    public Object getItem(int position) {
        return meal.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup parent) {
        Viewholder viewholder;
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.item_mead_details, parent, false);
            viewholder = new Viewholder();
            viewholder.tvName = (TextView) view.findViewById(R.id.tv_name_food);
            viewholder.tvNumber = (TextView) view.findViewById(R.id.tv_number_food);
            viewholder.iv_edit = (ImageView) view.findViewById(R.id.iv_edit);
            viewholder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            viewholder.iv_food = (ImageView) view.findViewById(R.id.iv_item_meal);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }
        viewholder.tvName.setText(meal.getItems().get(i).getFood().getName());
        String s = meal.getItems().get(i).getFood().getCount() + " " + meal.getItems().get(i).getFood().getUnit() + " ( " + meal.getItems().get(i).totalCalo() + " Calo" + " )";
        viewholder.tvNumber.setText(meal.getItems().get(i).getNumber() + "x" + s);
        StorageReference mref = sref.child("food/" + meal.getItems().get(i).getFood().getImageUrl());
        Glide.with(activity)
                .load(mref)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewholder.iv_food);
        final String idFood = meal.getItems().get(i).getFood().getId();
        final String date = meal.getDate();
        final String type = meal.getType();
        viewholder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = meal.getItems().get(i).getFood();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View view_dialog = activity.getLayoutInflater().inflate(R.layout.dialog_edit_food, null);
                ImageView iv_food, iv_plus, iv_sub;
                Spinner spinner;

                final TextView tv_name, tv_number_calo, tv_don_vi_food, tv_number_food;
                Button bt_add, bt_cancel;

                spinner = (Spinner) view_dialog.findViewById(R.id.spin_dialog_meal);
                ArrayAdapter<String> adapterspin;
                String spinmeal[] = {meal.getType()};
//
                adapterspin = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, spinmeal);
                adapterspin.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setAdapter(adapterspin);

                tv_name = (TextView) view_dialog.findViewById(R.id.tv_dialog_name);
                tv_don_vi_food = (TextView) view_dialog.findViewById(R.id.tv_dialog_don_vi_food);
                tv_number_calo = (TextView) view_dialog.findViewById(R.id.tv_dialog_number_calo);
                tv_number_food = (TextView) view_dialog.findViewById(R.id.tv_dialog_number_food);
                bt_add = (Button) view_dialog.findViewById(R.id.bt_dialog_add);
                bt_cancel = (Button) view_dialog.findViewById(R.id.bt_dialog_cancel);
                tv_name.setText(food.getName());

                iv_food = (ImageView) view_dialog.findViewById(R.id.iv_dialog_food);
                iv_plus = (ImageView) view_dialog.findViewById(R.id.iv_dialog_plus);
                iv_sub = (ImageView) view_dialog.findViewById(R.id.iv_dialog_sub);

                tv_number_calo.setText(food.getCalo() + " Calo");
                tv_don_vi_food.setText("  " + "x" + food.getCount() + " " + food.getUnit());
                tv_number_food.setText(meal.getItems().get(i).getNumber() + "");
                StorageReference mref = sref.child("food/" + meal.getItems().get(i).getFood().getImageUrl());
                Glide.with(activity)
//                        .using(new FirebaseImageLoader())
                        .load(mref)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_food);

                number = Integer.parseInt(tv_number_food.getText().toString());

                iv_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        number++;
                        tv_number_food.setText(number + "");
                        meal.getItems().get(i).setNumber(number);

                    }
                });
                iv_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (number > 1) {
                            number = number - 1;
                        } else {
                            number = 1;
                        }
                        tv_number_food.setText(number + "");
                        meal.getItems().get(i).setNumber(number);

                    }
                });


                builder.setView(view_dialog);


                final AlertDialog dialog = builder.create();
                dialog.show();
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();

                    }
                });
                bt_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MethodUtils methodUtils = new MethodUtils();
                        if (methodUtils.compareDate(date) == 1) {
                            dialog.cancel();
                            Toast.makeText(activity, "Ngày " + date + " đã qua, Bạn không thể thêm food", Toast.LENGTH_LONG).show();
                        } else {
                            meal.getItems().get(i).setNumber(Integer.parseInt(tv_number_food.getText().toString()));
                            ref.child(meal.getType()).child(date).updateChildren(meal.toMap());
                            if (meal.getType().equals(ConstantUtils.Breakfast)) {
                                ref.child("Statistic").child(date).child("totalBreakfast").setValue(meal.getTotalCalo());
                            }
                            if (meal.getType().equals(ConstantUtils.Lunch)) {
                                ref.child("Statistic").child(date).child("totalLunch").setValue(meal.getTotalCalo());
                            }
                            if (meal.getType().equals(ConstantUtils.Dinner)) {
                                ref.child("Statistic").child(date).child("totalDinner").setValue(meal.getTotalCalo());
                            }
                            if (meal.getType().equals(ConstantUtils.Snack)) {
                                ref.child("Statistic").child(date).child("totalSnack").setValue(meal.getTotalCalo());
                            }
                            notifyDataSetChanged();
                            Toast.makeText(activity, "Edit successfully", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }


                    }
                });


            }
        });

        viewholder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MethodUtils methodUtils = new MethodUtils();
                if (methodUtils.compareDate(date) == 1) {
                    Toast.makeText(activity, "Ngày " + date + " đã qua, Bạn không thể xóa food", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                    builder.setMessage("Do you really want to delete?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i1) {

//                                DataCenter dataCenter = new DataCenter();
//                                dataCenter.deleteItem(meal.getItems().get(i).getId(), date, type);
                                    meal.getItems().remove(i);
                                    ref.child(meal.getType()).child(date).updateChildren(meal.toMap());
                                    notifyDataSetChanged();
                                    if (meal.getType().equals(ConstantUtils.Breakfast)) {
                                        ref.child("Statistic").child(date).child("totalBreakfast").setValue(meal.getTotalCalo());
                                    }
                                    if (meal.getType().equals(ConstantUtils.Lunch)) {
                                        ref.child("Statistic").child(date).child("totalLunch").setValue(meal.getTotalCalo());
                                    }
                                    if (meal.getType().equals(ConstantUtils.Dinner)) {
                                        ref.child("Statistic").child(date).child("totalDinner").setValue(meal.getTotalCalo());
                                    }
                                    if (meal.getType().equals(ConstantUtils.Snack)) {
                                        ref.child("Statistic").child(date).child("totalSnack").setValue(meal.getTotalCalo());
                                    }
                                    Toast.makeText(activity, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layout_main, fragment);
        ft.commit();
    }

    private class Viewholder {
        TextView tvName, tvNumber;
        ImageView iv_edit, iv_delete, iv_food;
    }
}
