package com.example.canvala.ui.main.owner.transactions;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.canvala.R;
import com.example.canvala.data.api.AdminService;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.model.TransactionsModel;
import com.example.canvala.databinding.FragmentTransactionsAdminBinding;
import com.example.canvala.databinding.FragmentTransactionsOwnerBinding;
import com.example.canvala.ui.main.admin.adapter.TransactionsAdminAdapter;
import com.example.canvala.util.Constants;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsFragment extends Fragment {

    List<TransactionsModel> transactionsModelList;
    LinearLayoutManager linearLayoutManager;
    TransactionsAdminAdapter transactionsAdminAdapter;
    SharedPreferences sharedPreferences;
    AlertDialog progressDialog;
    AdminService adminService;
    String userId;

    private FragmentTransactionsOwnerBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionsOwnerBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);
        adminService = ApiConfig.getClient().create(AdminService.class);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Semua"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Belum Konfirmasi"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Terkonfirmasi"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Terkirim"));


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTransactions("all");
        listener();

    }

    private void listener() {

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

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.rvTransactions.setAdapter(null);
                    getTransactions("all");
                }else if (tab.getPosition() == 1) {
                    binding.rvTransactions.setAdapter(null);
                    getTransactions("BELUM KONFIRMASI");

                }else if (tab.getPosition() == 2) {
                    binding.rvTransactions.setAdapter(null);
                    getTransactions("TERKONFIRMASI");

                }else if (tab.getPosition() == 3) {
                    binding.rvTransactions.setAdapter(null);
                    getTransactions("TERKIRIM");

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterTransactions();
            }
        });
    }

    private void getTransactions(String status) {
        showProgressBar("Loading", "Memuat data transaksi", true);
        adminService.getAllTransactionsByStatus(status).enqueue(new Callback<List<TransactionsModel>>() {
            @Override
            public void onResponse(Call<List<TransactionsModel>> call, Response<List<TransactionsModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    transactionsModelList = response.body();
                    binding.tvEmpty.setVisibility(View.GONE);

                    transactionsAdminAdapter = new TransactionsAdminAdapter(getContext(), transactionsModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvTransactions.setLayoutManager(linearLayoutManager);
                    binding.rvTransactions.setAdapter(transactionsAdminAdapter);
                    binding.rvTransactions.setHasFixedSize(true);
                    showProgressBar("sd", "dssd", false);
                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    showProgressBar("adss", "sdsd", false);
                }
            }

            @Override
            public void onFailure(Call<List<TransactionsModel>> call, Throwable t) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                showProgressBar("adss", "sdsd", false);
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

    private void filter(String text) {
        ArrayList<TransactionsModel> filteredList = new ArrayList<>();
        for (TransactionsModel item : transactionsModelList) {
            if (item.getCode().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

            transactionsAdminAdapter.filter(filteredList);
            if (filteredList.isEmpty()) {

            }else {
                transactionsAdminAdapter.filter(filteredList);
            }
        }
    }

    private void filterTransactions(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_filter_transactions);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button btnBatal, btnDownload;
        TextView tvDateStart, tvDateEnd;
        tvDateStart = dialog.findViewById(R.id.tvDateStart);
        tvDateEnd = dialog.findViewById(R.id.tvDateEnd);
        btnDownload = dialog.findViewById(R.id.btnDownload);
        btnBatal = dialog.findViewById(R.id.btnBatal);
        dialog.show();
        tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker(tvDateEnd);
            }
        });

        tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker(tvDateStart);
            }
        });
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvDateStart.getText().toString().isEmpty()) {
                    tvDateStart.setError("Tidak boleh kosong");
                }else if (tvDateEnd.getText().toString().isEmpty()) {
                    tvDateEnd.setError("Tidak boleh kosong");
                }else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Constants.URL_DOWNLOAD_LAPORAN + tvDateStart.getText().toString() + "/" + tvDateEnd.getText().toString()));
                    startActivity(intent);

                }
            }
        });



        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void getDatePicker(TextView tvDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dateFormatted, monthFormatted;
                if (month < 10) {
                    monthFormatted = String.format("%02d", month + 1);
                }else {
                    monthFormatted = String.valueOf(month + 1);
                }

                if (dayOfMonth < 10) {
                    dateFormatted = String.format("%02d", dayOfMonth);
                }else {
                    dateFormatted = String.valueOf(dayOfMonth);
                }

                tvDate.setText(year + "-" + monthFormatted + "-" + dateFormatted);
            }
        });
        datePickerDialog.show();
    }
}