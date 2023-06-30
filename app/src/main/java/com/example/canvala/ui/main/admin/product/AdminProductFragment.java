package com.example.canvala.ui.main.admin.product;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.CartModel;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.UserModel;
import com.example.canvala.databinding.FragmentAdminProductBinding;
import com.example.canvala.ui.main.admin.adapter.ProductAdapter;
import com.example.canvala.ui.main.user.product.ProductKategoriFragment;
import com.example.canvala.util.Constants;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProductFragment extends Fragment implements ProductAdapter.OnButtonClickListener {

    private FragmentAdminProductBinding binding;
    List<ProductModel> productModelList;
    GridLayoutManager gridLayoutManager;
    ProductAdapter productAdapter;
    SharedPreferences sharedPreferences;
    UserService userService;
    AlertDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminProductBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userService = ApiConfig.getClient().create(UserService.class);




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllProduct();
        listener();
    }


    @Override
    public void onResume() {
        super.onResume();

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
    }

    private void listener() {

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


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
                    productAdapter.setOnButtonClickListener(AdminProductFragment.this);
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

    private void replace(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameUsers, fragment).addToBackStack(null).commit();
    }

    private void replaceKategori(String id) {
        Fragment fragment = new ProductKategoriFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameUsers, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onButtonClicked() {


    }
}