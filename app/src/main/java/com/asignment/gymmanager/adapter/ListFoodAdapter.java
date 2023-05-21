package com.asignment.gymmanager.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asignment.gymmanager.R;
import com.asignment.gymmanager.fragment.MealDetailFragment;
import com.asignment.gymmanager.model.Food;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.asignment.gymmanager.utils.MethodUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;



public class ListFoodAdapter extends BaseAdapter implements Filterable {
    private Activity activity;
    private String date = "";
    private int number = 1;
    private TextView tv_number_food_dialog;
    private String typeMeal_add = ConstantUtils.Breakfast;
    private ArrayList<Food> listfood = new ArrayList<>();
    private ArrayList<Food> template;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference sref = storage.getReference();
    private MethodUtils methodUtils = new MethodUtils();

    public ListFoodAdapter(Activity activity, ArrayList<Food> listfood) {
        this.activity = activity;
        this.listfood = listfood;

    }

    public ListFoodAdapter(Activity activity, String date, ArrayList<Food> listfood) {
        this.activity = activity;
        this.date = date;
        this.listfood = listfood;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getCount() {
        return listfood.size();
    }

    @Override
    public Object getItem(int position) {
        return listfood.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        //return null;
        Viewholder viewholder;
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.item_list_food, parent, false);
            viewholder = new Viewholder();
            viewholder.tvName = (TextView) view.findViewById(R.id.tv_name_food_list);
            viewholder.tvNumber = (TextView) view.findViewById(R.id.tv_number_food_list);
            viewholder.ivfood = (ImageView) view.findViewById(R.id.iv_food_list);
            viewholder.ivdetails = (ImageView) view.findViewById(R.id.iv_detail_food);
            view.setTag(viewholder);
        } else {
            viewholder = (Viewholder) view.getTag();
        }
        viewholder.tvName.setText(listfood.get(i).getName());
        String s = listfood.get(i).getCount() + " " + listfood.get(i).getUnit() + "-" + (listfood.get(i).getCalo() + " Calo");
        viewholder.tvNumber.setText(s);
        StorageReference mref = sref.child("food/" + listfood.get(i).getImageUrl());
        Glide.with(activity)
//                .using(new FirebaseImageLoader())
                .load(mref)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewholder.ivfood);
        final Food f = listfood.get(i);

        viewholder.ivdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("idFood", f.getId());
//                bundle.putString("date", date);
//                FoodDetailFragment fragment = new FoodDetailFragment();
//                fragment.setArguments(bundle);
//                replaceFragment(fragment);

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View view_dialog = activity.getLayoutInflater().inflate(R.layout.dialog_food_detail, null);
                ImageView iv_dialog_food = (ImageView) view_dialog.findViewById(R.id.iv_dialog_food_detail);
                TextView tv_dialog_name = (TextView) view_dialog.findViewById(R.id.tv_dialog_name_food_detail);
                TextView tv_calo = (TextView) view_dialog.findViewById(R.id.tv_dialog_calories_food_detail);
                TextView tv_pro = (TextView) view_dialog.findViewById(R.id.tv_dialog_protein_food_detail);
                TextView tv_fat = (TextView) view_dialog.findViewById(R.id.tv_dialog_fat_food_detail);
                TextView tv_carb = (TextView) view_dialog.findViewById(R.id.tv_dialog_carb_food_detail);
                TextView tv_number_unit = (TextView) view_dialog.findViewById(R.id.tv_dialog_food_detail_number_unit);
                Button btaddmeal = (Button) view_dialog.findViewById(R.id.bt_dialog_add_to_diary);
                Button btback = (Button) view_dialog.findViewById(R.id.bt_dialog_food_detail_back);
                StorageReference mref = sref.child("food/" + f.getImageUrl());
                Glide.with(activity)
//                        .using(new FirebaseImageLoader())
                        .load(mref)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_dialog_food);
                tv_dialog_name.setText(f.getName());
                tv_calo.setText(f.getCalo() + " " + ConstantUtils.unitCalo);
                tv_pro.setText(f.getProtein() + " " + ConstantUtils.unitPro);
                tv_fat.setText(f.getFat() + " " + ConstantUtils.unitFat);
                tv_carb.setText(f.getCarb() + " " + ConstantUtils.unitCarb);
                tv_number_unit.setText(f.getCount() + " " + f.getUnit());
                builder.setView(view_dialog);
                final AlertDialog dialog = builder.create();
                dialog.show();
                btaddmeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder_add = new AlertDialog.Builder(activity);
                        View view_dialog_add = activity.getLayoutInflater().inflate(R.layout.dialog_edit_food, null);
                        ImageView iv_food_add, iv_plus_add, iv_sub_add;
                        Spinner spinner;

                        Button bt_add_1, bt_cancel_1;
                        final TextView tv_name_add, tv_number_calo_add, tv_don_vi_food_add;
                        spinner = (Spinner) view_dialog_add.findViewById(R.id.spin_dialog_meal);
                        ArrayAdapter<String> adapterspin;
                        final String spinmeal[] = {ConstantUtils.Breakfast, ConstantUtils.Lunch, ConstantUtils.Dinner, ConstantUtils.Snack};
                        adapterspin = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, spinmeal);
                        adapterspin.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(adapterspin);
                        tv_name_add = (TextView) view_dialog_add.findViewById(R.id.tv_dialog_name);
                        bt_add_1 = (Button) view_dialog_add.findViewById(R.id.bt_dialog_add);
                        bt_cancel_1 = (Button) view_dialog_add.findViewById(R.id.bt_dialog_cancel);
                        tv_don_vi_food_add = (TextView) view_dialog_add.findViewById(R.id.tv_dialog_don_vi_food);
                        tv_number_calo_add = (TextView) view_dialog_add.findViewById(R.id.tv_dialog_number_calo);
                        tv_number_food_dialog = (TextView) view_dialog_add.findViewById(R.id.tv_dialog_number_food);
                        tv_name_add.setText(f.getName());
                        iv_food_add = (ImageView) view_dialog_add.findViewById(R.id.iv_dialog_food);
                        iv_plus_add = (ImageView) view_dialog_add.findViewById(R.id.iv_dialog_plus);
                        iv_sub_add = (ImageView) view_dialog_add.findViewById(R.id.iv_dialog_sub);
                        tv_number_calo_add.setText(f.getCalo() + " Calo");
                        tv_don_vi_food_add.setText("  " + "x" + f.getCount() + " " + f.getUnit());
                        tv_number_food_dialog.setText(1 + "");
                        StorageReference mref = sref.child("food/" + f.getImageUrl());
                        Glide.with(activity)
                                .load(mref)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_food_add);
                        iv_plus_add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                number = Integer.parseInt(tv_number_food_dialog.getText().toString());
                                number++;
                                tv_number_food_dialog.setText(number + "");


                            }
                        });

                        iv_sub_add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                number = Integer.parseInt(tv_number_food_dialog.getText().toString());
                                if (number > 1) {
                                    number = number - 1;
                                } else {
                                    number = 1;
                                }
                                tv_number_food_dialog.setText(number + "");
                            }
                        });
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                typeMeal_add = spinmeal[position];
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        builder_add.setView(view_dialog_add);
                        final AlertDialog dialog_add = builder_add.create();
                        dialog_add.show();
                        bt_add_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MethodUtils methodUtils = new MethodUtils();
                                if (methodUtils.compareDate(date) == 1) {
                                    Toast.makeText(activity, "Ngày " + date + " đã qua, Bạn không thể thêm food", Toast.LENGTH_LONG).show();
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("typeMeal", typeMeal_add);
                                    bundle.putString("date", methodUtils.getTimeNow());
                                    bundle.putString("idFood", f.getId());
                                    bundle.putString("number", tv_number_food_dialog.getText().toString());
                                    MealDetailFragment fragment = new MealDetailFragment();
                                    fragment.setArguments(bundle);
                                    replaceFragment(fragment);
                                }
                                dialog_add.cancel();
                                dialog.cancel();
                            }
                        });
                        bt_cancel_1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_add.cancel();
                            }
                        });
                    }
                });

                btback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });




            }
        });


        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(fragment.getClass().getName());
        ft.replace(R.id.layout_meal, fragment, ConstantUtils.FRAGMENT_TAG_LIST_FOOD);
        ft.commit();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults result = new FilterResults();
                final ArrayList<Food> listResult = new ArrayList<>();
                if (template == null) {
                    template = listfood;
                }
                if (charSequence != null) {
                    if (template != null && template.size() > 0) {
                        for (final Food f : template) {
                            if (f.getName().toLowerCase().contains(charSequence.toString())) {
                                listResult.add(f);
                            }
                        }
                    }
                    result.values = listResult;
                }
                return result;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listfood = (ArrayList<Food>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class Viewholder {
        TextView tvName, tvNumber;
        ImageView ivfood, ivdetails;
    }


}
