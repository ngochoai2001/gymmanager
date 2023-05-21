package com.asignment.gymmanager.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.asignment.gymmanager.R;
import com.asignment.gymmanager.model.User;
import com.asignment.gymmanager.model.UserType;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class InformationActivity extends AppCompatActivity {
    private EditText edtHeight, edtWeight, edtAge;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Button btnSave;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initView();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final ProgressDialog  dialog = Progr

                final ProgressDialog dialog = ProgressDialog.show(InformationActivity.this, "", "Saving...");
                String weight, height, age, gender;
                weight = edtWeight.getText().toString();
                height = edtHeight.getText().toString();
                age = edtAge.getText().toString();
                if (weight.equals("") || height.equals("") || age.equals("")) {
                    Toast.makeText(getApplicationContext(), "Hay dien day du thong tin", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> value = new HashMap<>();
                    value.put("height", height);
                    value.put("weight", weight);
                    value.put("age", age);
                    int goal, w, h;
                    int a;
                    w = Integer.parseInt(weight);
                    h = Integer.parseInt(height);
                    a = Integer.parseInt(age);
                    if (rbFemale.isChecked()) {
                        gender = rbFemale.getText().toString().trim();
                        value.put("gender", gender);
                        goal = (int) (((9.246 * w) + (3.098 * h) - (4.330 * a) + 88.362) * 1.55);
                        value.put("goal", goal);
                    } else {
                        gender = rbMale.getText().toString();
                        value.put("gender", gender);
                        goal = (int) (((13.397 * w) + (4.799 * h) - (5.677 * a) + 447.593) * 1.55);
                        value.put("goal", goal);
                    }
                    value.put("name", user.getDisplayName());
                    value.put("email", user.getEmail());
                    value.put("imageUrl", user.getPhotoUrl());
                    User u = new User(user.getUid(), user.getDisplayName(), a, user.getEmail(),
                            user.getPhotoUrl().toString(), gender, h, w, goal);
                    u.setUserType(UserType.MEMBER);
                    ref.child(user.getUid()).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(InformationActivity.this, MainActivity.class));
                            dialog.dismiss();
//                            finish();
                        }
                    });

                }
            }
        });

    }
    private void initView() {
        edtHeight = (EditText) findViewById(R.id.edt_init_height);
        edtAge = (EditText) findViewById(R.id.edt_init_age);
        edtWeight = (EditText) findViewById(R.id.edt_init_weight);
        btnSave = (Button) findViewById(R.id.btn_init_save);
        rgGender = (RadioGroup) findViewById(R.id.rg_init_gender);
        rbFemale = (RadioButton) findViewById(R.id.rb_init_female);
        rbMale = (RadioButton) findViewById(R.id.rb_init_male);
    }
}