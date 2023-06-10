package com.example.canvala.ui.main.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.AuthService;
import com.example.canvala.data.model.AuthModel;
import com.example.canvala.ui.main.user.UserMainActivity;
import com.example.canvala.util.Constants;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    AuthService authService;
    TextView tvDaftar;
    AlertDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authService = ApiConfig.getClient().create(AuthService.class);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvDaftar = findViewById(R.id.tvDaftar);

        if (sharedPreferences.getBoolean(Constants.SHARED_PREF_LOGGED, false)) {
            if (sharedPreferences.getString(Constants.SHARED_PREF_ROLE, null).equals("USER")) {
                startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                finish();

            }
        }

        listener();
    }

    private void listener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().isEmpty()) {
                    etEmail.setError("Email tidak boleh kosong");
                    etEmail.requestFocus();
                }else  if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Password tidak boleh kosong");
                    etPassword.requestFocus();
                }else {
                    login();
                }
            }
        });

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });
    }

    private void login() {
        showProgressBar("Loading", "Authentifikasi...", true);
        authService.login(etEmail.getText().toString(), etPassword.getText().toString()).enqueue(new Callback<AuthModel>() {
            @Override
            public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {

                    editor.putBoolean(Constants.SHARED_PREF_LOGGED, true);
                    editor.putString(Constants.SHARED_PREF_USER_ID, response.body().getUserId());
                    editor.putString(Constants.SHARED_PREF_ROLE, response.body().getRole());
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                    finish();

                    showProgressBar("dsd", "Dsd", false);

                }else {
                    showProgressBar("sdsd", "sdds", false);
                    showToast("error", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AuthModel> call, Throwable t) {
                showProgressBar("sdsd", "sdds", false);
                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }

    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
            Toasty.success(LoginActivity.this, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(LoginActivity.this, text, Toasty.LENGTH_SHORT).show();
        }
    }
}