package com.example.canvala.ui.main.user.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.CartModel;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.UserModel;
import com.example.canvala.databinding.FragmentUserHomeBinding;
import com.example.canvala.ui.main.auth.LoginActivity;
import com.example.canvala.ui.main.user.adapter.ProductAdapter;
import com.example.canvala.util.Constants;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeFragment extends Fragment {

    private FragmentUserHomeBinding binding;
    List<ProductModel> productModelList;
    GridLayoutManager gridLayoutManager;
    ProductAdapter productAdapter;
    SharedPreferences sharedPreferences;
    UserService userService;
    AlertDialog progressDialog;
    private String nama, userId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserHomeBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userService = ApiConfig.getClient().create(UserService.class);
        userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllProduct();
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        getProfile();
        getTotalCart();
    }

    private void getAllProduct() {
        showProgressBar("Loading", "Memuat data...", true);
        userService.getAllProduct().enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    productModelList = response.body();
                    productAdapter = new ProductAdapter(getContext(), productModelList);
                    gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                    binding.rvProduct.setLayoutManager(gridLayoutManager);
                    binding.rvProduct.setAdapter(productAdapter);
                    binding.rvProduct.setHasFixedSize(true);
                    showProgressBar("sdd", "dsd", false);
                }else {
                    showProgressBar("sdsd","sds", false);
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                showProgressBar("sdsd","sds", false);
                showToast("error", "Tidak ada koneksi internet");

            }
        });


    }

    private void filter(String text) {
        ArrayList<ProductModel> filteredList = new ArrayList<>();
        for (ProductModel item : productModelList) {
            if (item.getProduct_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

            productAdapter.filter(filteredList);
            if (filteredList.isEmpty()) {

            }else {
                productAdapter.filter(filteredList);
            }
        }

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


    private void getTotalCart() {
        showProgressBar("Loading", "Memuat data...", true);
        userService.getMyCart(userId).enqueue(new Callback<List<CartModel>>() {
            @Override
            public void onResponse(Call<List<CartModel>> call, Response<List<CartModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    binding.tvTotalNotif.setText(String.valueOf(response.body().size()));
                    showProgressBar("sds", "sdsd", false);
                }else {
                    binding.tvTotalNotif.setText("0");
                    showProgressBar("sds", "sdsd", false);

                }
            }

            @Override
            public void onFailure(Call<List<CartModel>> call, Throwable t) {
                binding.tvTotalNotif.setText("0");
                showProgressBar("sds", "sdsd", false);
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