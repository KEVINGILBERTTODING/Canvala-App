package com.example.canvala.ui.main.admin.transactions;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.databinding.FragmentDetailTransactionsBelumKonfirmasiBinding;
import com.example.canvala.databinding.FragmentDetailTransactionsBinding;
import com.example.canvala.ui.main.user.transactions.TransactionsFragment;
import com.example.canvala.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransactionsBelumKonfirmasiFragment extends Fragment {
    private EditText etPath;
    SharedPreferences sharedPreferences;
    UserService userService;
    private String userId;
    private File file;
    private AlertDialog progressDialog;
    String transaction_id;

    private FragmentDetailTransactionsBelumKonfirmasiBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailTransactionsBelumKonfirmasiBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userService = ApiConfig.getClient().create(UserService.class);
        userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);
        transaction_id = getArguments().getString("transaction_id");

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
            binding.btnPesan.setVisibility(View.VISIBLE);
        }else {
            binding.btnPesan.setVisibility(View.GONE);

        }

        listener();


    }

    private void listener() {
        binding.btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

}