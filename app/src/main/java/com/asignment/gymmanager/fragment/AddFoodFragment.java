package com.asignment.gymmanager.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.asignment.gymmanager.activity.MainActivity;
import com.asignment.gymmanager.R;
import com.asignment.gymmanager.model.Food;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.asignment.gymmanager.utils.MethodUtils;
import com.asignment.gymmanager.utils.OnBackPressedListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class AddFoodFragment extends Fragment implements OnBackPressedListener {
    private final static int CAMERA_REQUEST = 1;
    private final static int GALLERY_REQUEST = 0;
    private EditText et_name, et_unit, et_count, et_calo, et_pro, et_fat, et_carb;
    private LinearLayout layout_add;
    private Button bt_addd, bt_can;
    private ImageView iv_food;
    private TextView tv_title;
    private String url = "";
    private int hasImage = 0;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference sref = storage.getReference();
    private Bitmap bitmap;
    private Uri uri;
    private String date = "", typeMeal = "";

    public static AddFoodFragment newInstance() {
        AddFoodFragment fragment = new AddFoodFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        MainActivity.stateMeal = ConstantUtils.FRAGMENT_ADD_FOOD;
        ((MainActivity) getActivity()).updateTitle(MainActivity.page, MainActivity.stateMeal);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.dialog_add_food, container, false);
//        ((MainActivity) getActivity()).updateActionbar(true, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        return viewGroup;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnIntent);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case 1: {
                    if (requestCode == CAMERA_REQUEST) {
                        bitmap = (Bitmap) imageReturnIntent.getExtras().get("data");
                        iv_food.setImageBitmap(bitmap);
                        hasImage = 1;
                        break;
                    }
                }
                case 0: {
                    if (requestCode == GALLERY_REQUEST) {
                        uri = imageReturnIntent.getData();
                        hasImage = 2;
                        iv_food.setImageURI(uri);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.dialog_err);
        builder.setTitle("Lưu ý");
        builder.setMessage("- Bạn phải nhập đầy đủ thông tin food. Trong đó : \n" + "" +
                "- Unit : Đơn vị của food ( vd : ml, gram) \n" +
                "- Count : số lượng đơn vị,count là một số (vd : 10 gram)\n" +
                "- Calo, Protein, Fat, Carb : thông số của food (vd : 2.5)")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i1) {

                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        layout_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seclectedImage();
            }
        });


        bt_addd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasImage == 0) {
                    Toast.makeText(getActivity(), "Bạn chưa chọn ảnh", Toast.LENGTH_LONG).show();
                    return;
                }
                String name = et_name.getText().toString();
                String unit = et_unit.getText().toString();
                String count = et_count.getText().toString();
                String calo = et_calo.getText().toString();
                String pro = et_pro.getText().toString();
                String fat = et_fat.getText().toString();
                String carb = et_carb.getText().toString();
                MethodUtils methodUtils = new MethodUtils();
                if (methodUtils.readString(name) && methodUtils.readString(unit) && methodUtils.readInt(count) && methodUtils.readFloat(calo)
                        && methodUtils.readFloat(pro) && methodUtils.readFloat(carb) && methodUtils.readFloat(fat)) {
                    Food f = new Food();
                    final Bundle bundle = new Bundle();
                    bundle.putString("typeMeal", typeMeal);
                    bundle.putString("date", date);

                    f.setName(name);
                    f.setUnit(unit);
                    f.setCount(Integer.parseInt(count));
                    f.setCalo(Float.parseFloat(calo));
                    f.setProtein(Float.parseFloat(pro));
                    f.setFat(Float.parseFloat(fat));
                    f.setCarb(Float.parseFloat(carb));
                    f.setImageUrl(name + ".jpeg");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference mRef = ref.child("Food").push();
                    f.setId(mRef.getKey());
                    mRef.setValue(f.toMap());
                    StorageReference mref = sref.child("food").child(f.getImageUrl());
                    if (hasImage == 1) {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        byte[] data = bytes.toByteArray();

                        UploadTask uploadTask = mref.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Upload Fail", Toast.LENGTH_SHORT);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Fragment fragment = new ListFoodFragment().newInstance();
                                fragment.setArguments(bundle);
                                replaceFragment(fragment);
                                Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT);
                            }
                        });

                    } else if (hasImage == 2) {
                        UploadTask uploadTask = mref.putFile(uri);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Upload Fail", Toast.LENGTH_SHORT);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Fragment fragment = new ListFoodFragment().newInstance();
                                fragment.setArguments(bundle);
                                replaceFragment(fragment);
                                Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT);

                            }
                        });
                    }

                    Toast.makeText(getActivity(), "Add Success", Toast.LENGTH_LONG).show();

                } else {
                    String s = "";
                    if (!methodUtils.readString(name)) {
                        s = s + " Chưa nhập name food\n";


                    }
                    if (!methodUtils.readString(et_unit.getText() + "")) {
                        s = s + " Chưa nhập unit(g,ml)\n";

                    }
                    if (!methodUtils.readInt(count)) {
                        s = s + " Chưa nhập đúng dịnh dạng count \n";


                    }
                    if (!methodUtils.readFloat(calo)) {
                        s = s + " Chưa nhập đúng dịnh dạng calo \n";

                    }
                    if (!methodUtils.readFloat(pro)) {
                        s = s + " Chưa nhập đúng dịnh dạng protein\n";
                    }
                    if (!methodUtils.readFloat(fat)) {
                        s = s + " Chưa nhập đúng dịnh dạng fat\n";
                    }
                    if (!methodUtils.readFloat(carb)) {
                        s = s + " Chưa nhập đúng dịnh dạng carb\n";

                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setIcon(R.drawable.dialog_err);
                    builder.setTitle("Cảnh Báo");
                    builder.setMessage("- Bạn phải nhập đầy đủ thông tin food. Trong đó : \n" + "" +
                            "- Unit : Đơn vị của food ( vd : ml, gram) \n" +
                            "- Count : số lượng đơn vị,count là một số (vd : 10 gram)\n" +
                            "- Calo,Protein,Fat,Carb : thông số của food,là một số thập phân (vd : 2.5)\n" +
                            "- Lỗi : \n" +
                            "" + s)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i1) {
//                                DataCenter dataCenter = new DataCenter();
//                                dataCenter.deleteItem(meal.getItems().get(i).getId(), date, type);
                                    dialogInterface.cancel();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();


                }


//                ListFoodFragment listFoodFragment = new ListFoodFragment();
//
//                replaceFragment(listFoodFragment);

            }
        });
        bt_can.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("typeMeal", typeMeal);
                bundle.putString("date", date);
                ListFoodFragment fragment = new ListFoodFragment();
                fragment.setArguments(bundle);
                replaceFragment(fragment);

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        date = bundle.getString("date", "");
        typeMeal = bundle.getString("typeMeal", "");
        if (typeMeal.equals("")) {
            typeMeal = ConstantUtils.Breakfast;
        }
        if (date.equals("")) {
            MethodUtils methodUtils = new MethodUtils();
            date = methodUtils.getTimeNow();
        }
    }

    protected void seclectedImage() {
        final CharSequence[] items = {"Take Photo", "Choose From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");
        builder.setIcon(R.drawable.cam_64);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST);
                } else if (items[which].equals("Choose From Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_REQUEST);
                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void init() {
        et_name = (EditText) getView().findViewById(R.id.et_add_food_name);
        et_unit = (EditText) getView().findViewById(R.id.et_add_food_unit);
        et_count = (EditText) getView().findViewById(R.id.et_add_food_count);
        et_calo = (EditText) getView().findViewById(R.id.et_add_food_calo);
        et_pro = (EditText) getView().findViewById(R.id.et_add_food_protein);
        et_carb = (EditText) getView().findViewById(R.id.et_add_food_carb);
        et_fat = (EditText) getView().findViewById(R.id.et_add_food_fat);
        layout_add = (LinearLayout) getView().findViewById(R.id.layout_add_image);
        bt_addd = (Button) getView().findViewById(R.id.bt_dialog_add_food_add_food);
        bt_can = (Button) getView().findViewById(R.id.bt_dialog_add_food_cancel_food);
        iv_food = (ImageView) getView().findViewById(R.id.iv_dialog_add);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.layout_meal, fragment);
        ft.commit();
    }

    @Override
    public void doBack() {
        Bundle bundle = new Bundle();
        bundle.putString("typeMeal", typeMeal);
        bundle.putString("date", date);
        ListFoodFragment fragment = new ListFoodFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

}
