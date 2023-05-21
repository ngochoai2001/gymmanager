package com.asignment.gymmanager.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.asignment.gymmanager.R;
import com.asignment.gymmanager.dto.AdminRequest;
import com.asignment.gymmanager.model.admin.Admin;
import com.asignment.gymmanager.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText ausername, apass;
    private Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        initView();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                String username = ausername.getText().toString();
                String password = apass.getText().toString();
                AdminRequest admin = new AdminRequest(username, password);
                Call<Admin> result = RetrofitInstance.getRetrofit().getApiInterface().adminLogin(admin);
                result.enqueue(new Callback<Admin>() {
                    @Override
                    public void onResponse(Call<Admin> call, Response<Admin> response) {
                        Intent intent = new Intent(AdminLoginActivity.this, AdminActivity.class);
                        intent.putExtra("admin",response.body());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Admin> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        });
    }

    private void initView() {
        ausername = findViewById(R.id.ausername);
        apass = findViewById(R.id.apass);
        btn_login = findViewById(R.id.btn_login);
    }
}