package com.example.canvala.ui.main.user.transactions;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.data.model.TransactionsDetailModel;
import com.example.canvala.databinding.FragmentDetailTransactionsBinding;
import com.example.canvala.ui.main.admin.adapter.TransactionsDetailAdapter;
import com.example.canvala.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransactionsFragment extends Fragment {
    private EditText etPath;
    SharedPreferences sharedPreferences;
    UserService userService;
    private String userId;
    LinearLayoutManager linearLayoutManager;
    List<TransactionsDetailModel> transactionsDetailModelList;
    TransactionsDetailAdapter transactionsDetailAdapter;
    private File file;
    private AlertDialog progressDialog;
    String transaction_id;

    private FragmentDetailTransactionsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailTransactionsBinding.inflate(inflater, container, false);
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
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.layout_upload_bukti_tf);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                TextView tvNamaBank, tvNoRek, tvNama;
                etPath = dialog.findViewById(R.id.etPathImage);
                Button btnPilih, btnUpload, btnBatal;
                btnPilih = dialog.findViewById(R.id.btnPilih);
                btnUpload = dialog.findViewById(R.id.btnUpload);
                btnBatal = dialog.findViewById(R.id.btnBatal);
                tvNamaBank = dialog.findViewById(R.id.tvBankName);
                tvNoRek = dialog.findViewById(R.id.tvNoRek);
                tvNama = dialog.findViewById(R.id.tvNama);


                tvNamaBank.setText(getArguments().getString("nama_bank"));
                tvNoRek.setText(getArguments().getString("no_rek"));
                tvNama.setText(getArguments().getString("rekening_name"));


                dialog.show();

                btnPilih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 1);
                    }
                });

                btnUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etPath.getText().toString().isEmpty()) {
                            etPath.setError("Anda belum memilih bukti pembayaran");
                            etPath.requestFocus();
                        }else {

                            showProgressBar("Loading", "Validasi Bukti...", true);
                            HashMap map = new HashMap();
                            map.put("id_transactions", RequestBody.create(MediaType.parse("text/plain"), transaction_id));
                            RequestBody requestBody = RequestBody.create(MediaType.parse("iamge/*"), file);
                            MultipartBody.Part image = MultipartBody.Part.createFormData("bukti", file.getName(), requestBody);
                            userService.uploadBuktiTransfer(map, image).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    if (response.isSuccessful() && response.body().getStatus() == 200) {
                                        showProgressBar("SD", "dsd", false);
                                        Dialog dialog2 = new Dialog(getContext());
                                        dialog2.setContentView(R.layout.layout_alert_successs);
                                        dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                        Button btnOke = dialog2.findViewById(R.id.btnOke);
                                        dialog2.show();


                                        btnOke.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog2.dismiss();
                                            }
                                        });
                                        dialog.dismiss();

                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.frameUsers, new TransactionsFragment()).commit();
                                    }else {
                                        showProgressBar("ds", "ds",false);
                                        showToast("error", response.body().getMessage());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    showProgressBar("ds", "ds",false);
                                    showToast("error", "Tidak ada koneksi internet");

                                }
                            });

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
        });
    }


    private void getFormatRupiah(TextView tvText, String number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        String price = decimalFormat.format(Integer.parseInt(number));
        tvText.setText("Rp. " + price);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String pdfPath = getRealPathFromUri(uri);
                file = new File(pdfPath);
                etPath.setText(file.getName());

            }
        }
    }


    public String getRealPathFromUri(Uri uri) {
        String filePath = "";
        if (getContext().getContentResolver() != null) {
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                File file = new File(getContext().getCacheDir(), getFileName(uri));
                writeFile(inputStream, file);
                filePath = file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void writeFile(InputStream inputStream, File file) throws IOException {
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
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

    private void getTransactionsDetail() {
        showProgressBar("Loading", "Memuat data....", true);
        userService.getDetailTransactions(transaction_id).enqueue(new Callback<List<TransactionsDetailModel>>() {
            @Override
            public void onResponse(Call<List<TransactionsDetailModel>> call, Response<List<TransactionsDetailModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    transactionsDetailModelList = response.body();
                    transactionsDetailAdapter = new TransactionsDetailAdapter(getContext(), transactionsDetailModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvProduct.setLayoutManager(linearLayoutManager);
                    binding.rvProduct.setAdapter(transactionsDetailAdapter);
                    binding.rvProduct.setHasFixedSize(true);
                    showProgressBar("Sdd", "Dds", false);
                }else {
                    showProgressBar("Sds", "Dsds", false);
                    showToast("error", "Gagal memuat detail transaksi");
                }
            }

            @Override
            public void onFailure(Call<List<TransactionsDetailModel>> call, Throwable t) {
                showProgressBar("Sds", "Dsds", false);
                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }

}