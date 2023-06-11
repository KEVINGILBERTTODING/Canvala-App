package com.example.canvala.ui.main.user.cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.CartModel;
import com.example.canvala.data.model.InformationModel;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.RekeningModel;
import com.example.canvala.data.model.UserModel;
import com.example.canvala.databinding.FragmentCartBinding;
import com.example.canvala.ui.main.user.adapter.CartAdapter;
import com.example.canvala.ui.main.user.adapter.SpinnerRekeningAdapter;
import com.example.canvala.util.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements CartAdapter.OnButtonClickListener {
    private FragmentCartBinding binding;
    List<CartModel> cartModelList;
    CartAdapter cartAdapter;
    List<RekeningModel> rekeningModelList;
    LinearLayoutManager linearLayoutManager;
    UserService userService;
    SpinnerRekeningAdapter spinnerRekeningAdapter;
    String [] opsiKota = {"JOGJA", "SEMARANG"};
    String userId;
    SharedPreferences sharedPreferences;
    AlertDialog progressDialog;
    String kota, rekening;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);
        userService = ApiConfig.getClient().create(UserService.class);

        ArrayAdapter adapterKota = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiKota);
        adapterKota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spKota.setAdapter(adapterKota);

        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        getCart();
        listener();
        getProfile();
        getAllRekening();

    }

    private void listener() {
        binding.spKota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kota = opsiKota[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.icArrowDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lrAlamat.setVisibility(View.VISIBLE);
                binding.icArrowDown.setVisibility(View.GONE);
                binding.icArrowUp.setVisibility(View.VISIBLE);
            }
        });

        binding.icArrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lrAlamat.setVisibility(View.GONE);
                binding.icArrowUp.setVisibility(View.GONE);
                binding.icArrowDown.setVisibility(View.VISIBLE);
            }
        });
        binding.spPembayaran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rekening = String.valueOf(spinnerRekeningAdapter.getRekeningId(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
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
                    cartAdapter.setOnButtonClickListener(CartFragment.this);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvProduct.setLayoutManager(linearLayoutManager);
                    binding.rvProduct.setAdapter(cartAdapter);
                    binding.rvProduct.setHasFixedSize(true);
                    getInformationOrder();
                    binding.btnPesan.setEnabled(true);
                    binding.lrBody.setVisibility(View.VISIBLE);
                    showProgressBar("adasd", "ssds", false);
                }else {
                    showProgressBar("Sds", "dsd", false);
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.btnPesan.setVisibility(View.GONE);
                    binding.lrBody.setVisibility(View.GONE);
                    binding.tvTotalPembayaran.setText("-");


                }
            }

            @Override
            public void onFailure(Call<List<CartModel>> call, Throwable t) {
                showProgressBar("Sds", "dsd", false);
                binding.tvEmpty.setVisibility(View.GONE);
                binding.lrBody.setVisibility(View.GONE);
                showToast("error", "Tidak ada koneksi internet");
                binding.btnPesan.setEnabled(false);


            }
        });


    }

    private void getAllRekening() {
        showProgressBar("Loading", "Memuat data...", true);
        userService.getallrekening().enqueue(new Callback<List<RekeningModel>>() {
            @Override
            public void onResponse(Call<List<RekeningModel>> call, Response<List<RekeningModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    rekeningModelList = response.body();
                    spinnerRekeningAdapter = new SpinnerRekeningAdapter(getContext(), rekeningModelList);
                    binding.spPembayaran.setAdapter(spinnerRekeningAdapter);
                    showProgressBar("sds", "sds", false);
                    binding.btnPesan.setEnabled(true);
                }else {
                    showProgressBar("sds", "sds", false);
                    binding.btnPesan.setEnabled(false);
                    showToast("d", "Tidak dapat memuat metode pembayaran");
                }
            }

            @Override
            public void onFailure(Call<List<RekeningModel>> call, Throwable t) {
                showProgressBar("sds", "sds", false);
                binding.btnPesan.setEnabled(false);
                showToast("d", "Tidak ada koneksi internet");

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

    private void filter(String text) {
        ArrayList<CartModel> filteredList = new ArrayList<>();
        for (CartModel item : cartModelList) {
            if (item.getProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

            cartAdapter.filter(filteredList);
            if (filteredList.isEmpty()) {

            }else {
                cartAdapter.filter(filteredList);
            }
        }
    }

    private void getProfile() {
        showProgressBar("Loading", "Memuat data...", true);
        userService.getMyProfile(userId).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String replacedString = response.body().getAddress().replaceAll("<p>|</p>|&nbsp;|\n", "");
                    binding.etAlamat.setText(replacedString);
                    binding.etTelepon.setText(response.body().getPhoneNumber());
                    binding.etKodePos.setText(response.body().getPostalCode());
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

    private void getInformationOrder() {
        showProgressBar("Loading", "Memuat informasi pesanan...", true);
        userService.getInformationOrder(userId).enqueue(new Callback<InformationModel>() {
            @Override
            public void onResponse(Call<InformationModel> call, Response<InformationModel> response) {
                if (response.isSuccessful() && response.body() != null) {

                    binding.tvJumlahProduk.setText(response.body().getQty() + " Produk");
                    binding.tvBeratBarang.setText(response.body().getBerat() + " Kilogram");
                    getFormatRupiah(binding.tvNominal, response.body().getHargaTotal());
                    getFormatRupiah(binding.tvTotalPembayaran, response.body().getHargaTotal());
                    binding.btnPesan.setEnabled(true);

                    // sembunyikan
                    if (response.body().getQty() <= 99){
                        binding.lrProdukDibawah100.setVisibility(View.VISIBLE);
                    }else if (response.body().getQty() >= 100) {
                        binding.lrProdukDiatas100.setVisibility(View.VISIBLE);
                    }
                    showProgressBar("sds", "Sd", false);
                }else {
                    showProgressBar("sds", "Sd", false);
                    showToast("er", "Gagal memuat informasi pembelian");
                    binding.btnPesan.setEnabled(false);

                }
            }

            @Override
            public void onFailure(Call<InformationModel> call, Throwable t) {
                showProgressBar("sds", "Sd", false);
                showToast("er", "Tidak ada koneksi internet");
                binding.btnPesan.setEnabled(false);

            }
        });

    }

    private void getFormatRupiah(TextView tvText, Integer nominal) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        String price = decimalFormat.format(nominal);
        tvText.setText("Rp. " + price);
    }

    @Override
    public void onButtonClicked() {
        getCart();
    }
}