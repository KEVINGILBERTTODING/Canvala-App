package com.example.canvala.ui.main.admin.product;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.example.canvala.data.api.AdminService;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.model.CategoriesModel;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.databinding.FragmentAdminUpdateProductBinding;
import com.example.canvala.ui.main.admin.adapter.SpinnerCategoriesAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUpdateProductFragment extends Fragment {

    private FragmentAdminUpdateProductBinding binding;
    private AdminService adminService;
    private String productId;
    private File file;
    private AlertDialog progressDialog;
    private SpinnerCategoriesAdapter spinnerCategoriesAdapter;
    private List<CategoriesModel> categoriesModelList;
    private Integer categoriesId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminUpdateProductBinding.inflate(inflater, container, false);
        adminService = ApiConfig.getClient().create(AdminService.class);
        productId = getArguments().getString("product_id");




        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDetailProduct();
        getCategoris();
        listener();
    }

    private void listener() {
        binding.btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        binding.btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });

        binding.spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriesId = Integer.parseInt(spinnerCategoriesAdapter.getCategoriesId(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateProduct() {
        if (binding.etNamaProduk.getText().toString().isEmpty()) {
            binding.etNamaProduk.setError("Tidak boleh kosong");
            binding.etNamaProduk.requestFocus();
        }else  if (binding.etHargaProduk.getText().toString().isEmpty()) {
            binding.etHargaProduk.setError("Tidak boleh kosong");
            binding.etHargaProduk.requestFocus();
        }else  if (binding.etStok.getText().toString().isEmpty()) {
            binding.etStok.setError("Tidak boleh kosong");
            binding.etStok.requestFocus();
        }else  if (binding.etDeskripsi.getText().toString().isEmpty()) {
            binding.etDeskripsi.setError("Tidak boleh kosong");
            binding.etDeskripsi.requestFocus();
        }else {
            if (binding.etPathImage.getText().toString().isEmpty()) { // jika gambar priduct tidak di ganti
                HashMap map = new HashMap();
                map.put("product_name", RequestBody.create(MediaType.parse("plain/text"), binding.etNamaProduk.getText().toString()));
                map.put("price", RequestBody.create(MediaType.parse("plain/text"), binding.etHargaProduk.getText().toString()));
                map.put("description", RequestBody.create(MediaType.parse("plain/text"), binding.etDeskripsi.getText().toString()));
                map.put("stock", RequestBody.create(MediaType.parse("plain/text"), binding.etStok.getText().toString()));
                map.put("category_id", RequestBody.create(MediaType.parse("plain/text"), String.valueOf(categoriesId)));
                map.put("status", RequestBody.create(MediaType.parse("plain/text"), "false"));
                map.put("product_id", RequestBody.create(MediaType.parse("plain/text"), productId));

                showProgressBar("Loading", "Menyimpan perubahan...", true);
                adminService.updateProduct(map).enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        showProgressBar("s", "s", false);
                        if (response.isSuccessful() && response.body().getStatus() == 200) {
                            showToast("success", "Berhasil menyimpan perubahan");
                            getActivity().onBackPressed();
                        }else {
                            showToast("err", response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        showProgressBar("s", "s", false);
                        showToast("err", "Tidak ada koneksi internet");
                    }
                });

            }else {
                HashMap map = new HashMap();
                map.put("product_name", RequestBody.create(MediaType.parse("plain/text"), binding.etNamaProduk.getText().toString()));
                map.put("price", RequestBody.create(MediaType.parse("plain/text"), binding.etHargaProduk.getText().toString()));
                map.put("description", RequestBody.create(MediaType.parse("plain/text"), binding.etDeskripsi.getText().toString()));
                map.put("stock", RequestBody.create(MediaType.parse("plain/text"), binding.etStok.getText().toString()));
                map.put("category_id", RequestBody.create(MediaType.parse("plain/text"), String.valueOf(categoriesId)));
                map.put("status", RequestBody.create(MediaType.parse("plain/text"), "true"));
                map.put("product_id", RequestBody.create(MediaType.parse("plain/text"), productId));
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("foto", file.getName(), requestBody);

                showProgressBar("Loading", "Menyimpan perubahan...", true);
                adminService.updateProductImage(map, filePart).enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        showProgressBar("s", "s", false);
                        if (response.isSuccessful() && response.body().getStatus() == 200) {
                            showToast("success", "Berhasil menyimpan perubahan");
                            getActivity().onBackPressed();
                        }else {
                            showToast("err", response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        showProgressBar("s", "s", false);
                        showToast("err", "Tidak ada koneksi internet");
                    }
                });

            }

        }
    }

    private void getCategoris() {
        showProgressBar("Loading", "Memuat data categories...", true);
        adminService.getCategories().enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                showProgressBar("s", "S", false);
                if (response.isSuccessful() && response.body() != null) {
                    categoriesModelList = response.body();
                    spinnerCategoriesAdapter = new SpinnerCategoriesAdapter(getContext(), categoriesModelList);
                    binding.spCategories.setAdapter(spinnerCategoriesAdapter);


                }else {
                    showToast("err", "Gagal memuat data product");

                }
            }

            @Override
            public void onFailure(Call<List<CategoriesModel>> call, Throwable t) {
                showProgressBar("s", "S", false);
                showToast("err", "Tidak ada koneksi internet");



            }
        });
    }

    private void getDetailProduct(){
        showProgressBar("Loading", "Memuat product...", true);
        adminService.getProductById(productId).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                showProgressBar("s", "S", false);
                if (response.isSuccessful() && response.body() != null) {
                    binding.etNamaProduk.setText(response.body().getProduct_name());
                    binding.etHargaProduk.setText(String.valueOf(response.body().getPrice()));
                    binding.etStok.setText(String.valueOf(response.body().getStock()));
                    binding.etDeskripsi.setText(response.body().getDescriptions());

                    Glide.with(getContext()).load(response.body().getPhotos())
                            .into(binding.ivProduct);
                }
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String pdfPath = getRealPathFromUri(uri);
                file = new File(pdfPath);
                binding.etPathImage.setVisibility(View.VISIBLE);
                binding.etPathImage.setText(file.getName());
                binding.ivProduct.setImageURI(uri);
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


}