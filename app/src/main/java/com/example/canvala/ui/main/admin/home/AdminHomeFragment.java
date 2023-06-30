package com.example.canvala.ui.main.admin.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.UserModel;
import com.example.canvala.databinding.FragmentAdminHomeFragmnetBinding;
import com.example.canvala.ui.main.admin.Categories.AdminCategoriesFragment;
import com.example.canvala.ui.main.admin.product.AdminProductFragment;
import com.example.canvala.ui.main.admin.rekening.AdminRekeningFragment;
import com.example.canvala.ui.main.admin.transactions.TransactionsFragment;
import com.example.canvala.ui.main.admin.users.AdminUsersFragment;
import com.example.canvala.ui.main.auth.LoginActivity;
import com.example.canvala.util.Constants;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeFragment extends Fragment {

    private FragmentAdminHomeFragmnetBinding binding;
    AlertDialog progressDialog;
    UserService userService;
    SharedPreferences sharedPreferences;
    String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentAdminHomeFragmnetBinding.inflate(inflater, container, false);
         sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
         userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);
         userService = ApiConfig.getClient().create(UserService.class);

         return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getProfile();
    }

    private void listener() {
        binding.cvMenuTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new TransactionsFragment());

            }
        });
        binding.cvMenuKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new AdminCategoriesFragment());
            }
        });
        binding.cvMenuRekening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new AdminRekeningFragment());
            }
        });

        binding.cvMenuUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new AdminUsersFragment());
            }
        });

        binding.cvMenuProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new AdminProductFragment());
            }
        });
    }

    private void getProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        userService.getMyProfile(userId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    binding.tvnama.setText("Hai, " +response.body().getName());

                    showProgressBar("dsds", "Sdsd",false);
                }else {
                    showProgressBar("dsds", "Sdsd",false);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                showProgressBar("dsds", "Sdsd",false);
                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }


    private void replace(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, fragment)
                .addToBackStack(null).commit();

    }

    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
            Toasty.success(getContext(), text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), text, Toasty.LENGTH_SHORT).show();
        }
    }

}