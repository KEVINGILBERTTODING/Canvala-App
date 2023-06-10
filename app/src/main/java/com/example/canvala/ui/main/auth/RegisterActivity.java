package com.example.canvala.ui.main.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.ResponseModel;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText etEmail, etNama, etPass, etPassKonfir, etTelepon;
    Button btnDaftar;
    AlertDialog progressDialog;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etNama = findViewById(R.id.etNama);
        etPass = findViewById(R.id.etPassword);
        etPassKonfir = findViewById(R.id.etKonfirPass);
        etTelepon = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnDaftar = findViewById(R.id.btnDaftar);
        userService = ApiConfig.getClient().create(UserService.class);

        listener();
    }

    private void listener() {
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Email tidak boleh kosong");
                    etEmail.requestFocus();

                }else  if (etNama.getText().toString().isEmpty()) {
                    etNama.setError("Nama tidak boleh kosong");
                    etNama.requestFocus();

                }else  if (etPass.getText().toString().isEmpty()) {
                    etPass.setError("Password tidak boleh kosong");
                    etPass.requestFocus();
                }else  if (etPassKonfir.getText().toString().isEmpty()) {
                    etPassKonfir.setError("Password konfirmasi tidak boleh kosong");
                    etPassKonfir.requestFocus();
                } else if (!etPass.getText().toString().equals(etPassKonfir.getText().toString())) {
                    etPassKonfir.setError("Password dan password konfirmasi tidak sama");
                    etPassKonfir.requestFocus();
                }else {
                    simpanData();
                }
            }


        });
    }

    private void simpanData() {
        showProgressBar("Loading", "Menyimpan data...", true);
        userService.register(
                etEmail.getText().toString(),
                etNama.getText().toString(),
                etPass.getText().toString(),
                etTelepon.getText().toString()
        ).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    showProgressBar("Sds", "Sdsd", false);
                    showToast("success", response.body().getMessage());
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();

                }else {
                    showProgressBar("Sds", "Sdsd", false);
                    showToast("error", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("Sds", "Sdsd", false);
                showToast("error", "Tidak ada koneksi internet");
                Log.e("error", "onFailure: ", t );

            }
        });

    }


    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle(title);
                builder.setMessage(message);
                builder.setCancelable(false);
                progressDialog = builder.create();
            }
            progressDialog.show(); // Menampilkan progress dialog
        } else {
            // Menyembunyikan progress dialog jika ada
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    private void showToast(String jenis, String text) {
        if (jenis.equals("success")) {
            Toasty.success(RegisterActivity.this, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(RegisterActivity.this, text, Toasty.LENGTH_SHORT).show();
        }
    }
}