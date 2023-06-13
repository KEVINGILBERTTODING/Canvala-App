package com.example.canvala.ui.main.admin.transactions;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.canvala.R;
import com.example.canvala.data.api.AdminService;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.databinding.FragmentDetailTransactionsAdminBinding;
import com.example.canvala.util.Constants;

import java.io.File;
import java.text.DecimalFormat;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransactionsAdminFragment extends Fragment {
    SharedPreferences sharedPreferences;
    UserService userService;
    AdminService adminService;
    private AlertDialog progressDialog;
    String transaction_id;


    private FragmentDetailTransactionsAdminBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailTransactionsAdminBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userService = ApiConfig.getClient().create(UserService.class);
        transaction_id = getArguments().getString("transaction_id");
        adminService = ApiConfig.getClient().create(AdminService.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvCodeTransaksi.setText(getArguments().getString("code_transactions"));
        binding.tvStatus.setText(getArguments().getString("status"));
        binding.etKota.setText(getArguments().getString("kota"));
        binding.etTelepon.setText(getArguments().getString("telepon"));
        binding.etKodePos.setText(getArguments().getString("kode_pos"));
        binding.etTelepon.setText(getArguments().getString("telepon"));
        binding.etAlamat.setText(getArguments().getString("alamat"));
        binding.tvMetodePembayaran.setText(getArguments().getString("nama_bank"));
        binding.tvBeratBarang.setText(getArguments().getString("berat_total"));
        binding.tvJumlahProduk.setText(getArguments().getString("berat_total"));
        binding.etNamaLengkap.setText(getArguments().getString("nama_lengkap"));

        getFormatRupiah(binding.tvNominal, getArguments().getString("total"));
        getFormatRupiah(binding.tvTotalPembayaran, getArguments().getString("total"));


        if (getArguments().getString("bukti_tf").equals("")) {
            binding.ivBukti.setVisibility(View.GONE);
            binding.btnPesan.setVisibility(View.GONE);
        }else {
            binding.ivBukti.setVisibility(View.VISIBLE);
            Glide.with(getContext())
                    .load(Constants.URL_PRODUCT + getArguments().getString("bukti_tf"))
                    .fitCenter().into(binding.ivBukti);

            if (getArguments().getString("status").equals("BELUM KONFIRMASI")){
                binding.btnPesan.setText("Konfirmasi");

            }else if (getArguments().getString("status").equals("TERKONFIRMASI")) {
                binding.btnPesan.setText("Terkirim");
            }else if (getArguments().getString("status").equals("TERKIRIM")) {
                binding.btnPesan.setVisibility(View.GONE);
            }
        }





        listener();


    }

    private void listener() {
        binding.btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        binding.ivBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PreviewImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("image", getArguments().getString("bukti_tf"));
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, fragment)
                        .addToBackStack(null).commit();
            }
        });

        if (getArguments().getString("status").equals("BELUM KONFIRMASI")) {
            binding.btnPesan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    konfirmasi();
                }
            });
        }
    }


    private void getFormatRupiah(TextView tvText, String number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        String price = decimalFormat.format(Integer.parseInt(number));
        tvText.setText("Rp. " + price);
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

    private void konfirmasi() {
        showProgressBar("Loading", "Konfirmasi transaksi", true);
        adminService.konfirmasiTrans(transaction_id).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    showProgressBar("dsd","sd", false);
                    showToast("success", "Berhasil konfirmasi transaksi");
                    getActivity().onBackPressed();
                }else {
                    showProgressBar("dsd","sd", false);
                    showToast("error", "Gagal konfirmasi transaksi");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                showProgressBar("dsd","sd", false);
                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }

}