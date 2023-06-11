package com.example.canvala.ui.main.user.cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.CartModel;
import com.example.canvala.databinding.FragmentCartBinding;
import com.example.canvala.ui.main.user.adapter.CartAdapter;
import com.example.canvala.util.Constants;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    List<CartModel> cartModelList;
    CartAdapter cartAdapter;
    LinearLayoutManager linearLayoutManager;
    UserService userService;
    String userId;
    SharedPreferences sharedPreferences;
    AlertDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);
        userService = ApiConfig.getClient().create(UserService.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCart();
    }

    private void getCart() {
        showProgressBar("Loading", "Memuat data...", true);
        userService.getMyCart(userId).enqueue(new Callback<List<CartModel>>() {
            @Override
            public void onResponse(Call<List<CartModel>> call, Response<List<CartModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    binding.tvEmpty.setVisibility(View.GONE);
                    cartModelList = response.body();
                    cartAdapter = new CartAdapter(getContext(), cartModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvProduct.setLayoutManager(linearLayoutManager);
                    binding.rvProduct.setAdapter(cartAdapter);
                    binding.rvProduct.setHasFixedSize(true);
                    showProgressBar("adasd", "ssds", false);
                }else {
                    showProgressBar("Sds", "dsd", false);
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<CartModel>> call, Throwable t) {
                showProgressBar("Sds", "dsd", false);
                binding.tvEmpty.setVisibility(View.GONE);
                showToast("error", "Tidak ada koneksi internet");

            }
        });


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