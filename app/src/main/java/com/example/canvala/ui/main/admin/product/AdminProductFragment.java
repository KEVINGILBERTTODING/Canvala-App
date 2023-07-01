package com.example.canvala.ui.main.admin.product;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.canvala.R;
import com.example.canvala.data.api.AdminService;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.CartModel;
import com.example.canvala.data.model.CategoriesModel;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.data.model.UserModel;
import com.example.canvala.databinding.FragmentAdminProductBinding;
import com.example.canvala.ui.main.admin.adapter.ProductAdapter;
import com.example.canvala.ui.main.admin.adapter.SpinnerCategoriesAdapter;
import com.example.canvala.ui.main.user.product.ProductKategoriFragment;
import com.example.canvala.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class AdminProductFragment extends Fragment {

    private FragmentAdminProductBinding binding;
    List<ProductModel> productModelList;
    GridLayoutManager gridLayoutManager;
    ProductAdapter productAdapter;
    private List<CategoriesModel> categoriesModelList;
    SharedPreferences sharedPreferences;
    private AdminService adminService;
    private SpinnerCategoriesAdapter spinnerCategoriesAdapter;
    UserService userService;
    AlertDialog progressDialog;
    private Integer categoriesId;
    private EditText etPathImage;
    private File file;
    private Spinner spCategories;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminProductBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userService = ApiConfig.getClient().create(UserService.class);
        adminService = ApiConfig.getClient().create(AdminService.class);




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getAllProduct();
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

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProduct();

            }
        });




    }




    private void insertProduct() {
        Dialog dialogInsertProduct = new Dialog(getContext());
        dialogInsertProduct.setContentView(R.layout.layout_insert_product);
        dialogInsertProduct.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final EditText etNamaProduct, etHargaProduct, etStok, etDeskripsiProduk;
        spCategories = dialogInsertProduct.findViewById(R.id.spCategories);
        etPathImage = dialogInsertProduct.findViewById(R.id.etPathImage);
        etNamaProduct = dialogInsertProduct.findViewById(R.id.etNamaProduk);
        etHargaProduct = dialogInsertProduct.findViewById(R.id.etHargaProduk);
        etStok = dialogInsertProduct.findViewById(R.id.etStok);
        etDeskripsiProduk = dialogInsertProduct.findViewById(R.id.etDeskripsi);
        Button btnPilih, btnSimpan, btnBatal;
        btnPilih = dialogInsertProduct.findViewById(R.id.btnPilih);
        btnSimpan = dialogInsertProduct.findViewById(R.id.btnSimpan);
        btnBatal = dialogInsertProduct.findViewById(R.id.btnBatal);
        dialogInsertProduct.show();

        getCategoris();

        spCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriesId = Integer.parseInt(spinnerCategoriesAdapter.getCategoriesId(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInsertProduct.dismiss();
            }
        });



        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNamaProduct.getText().toString().isEmpty()) {
                    etNamaProduct.setError("Tidak boleh kosong");
                    etNamaProduct.requestFocus();
                }else  if (etHargaProduct.getText().toString().isEmpty()) {
                    etHargaProduct.setError("Tidak boleh kosong");
                    etHargaProduct.requestFocus();
                }else  if (etStok.getText().toString().isEmpty()) {
                    etStok.setError("Tidak boleh kosong");
                    etStok.requestFocus();
                }else  if (etDeskripsiProduk.getText().toString().isEmpty()) {
                    etDeskripsiProduk.setError("Tidak boleh kosong");
                    etDeskripsiProduk.requestFocus();
                }else  if (etPathImage.getText().toString().isEmpty()) {
                    showToast("err", "Anda belum memilih gambar produk");
                }else {

                    HashMap map = new HashMap();
                    map.put("product_name", RequestBody.create(MediaType.parse("plain/text"), etNamaProduct.getText().toString()));
                    map.put("price", RequestBody.create(MediaType.parse("plain/text"), etHargaProduct.getText().toString()));
                    map.put("description", RequestBody.create(MediaType.parse("plain/text"), etDeskripsiProduk.getText().toString()));
                    map.put("stock", RequestBody.create(MediaType.parse("plain/text"), etStok.getText().toString()));
                    map.put("category_id", RequestBody.create(MediaType.parse("plain/text"), String.valueOf(categoriesId)));



                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("foto", file.getName(), requestBody);


                    showProgressBar("Loading", "Menyimpan produk baru...", true);
                    adminService.insertPorduk(map, filePart

                    ).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            showProgressBar("s", "S", false);
                            if (response.isSuccessful() && response.body().getStatus() == 200) {
                                showToast("success", "Berhasil menambahkan produk baru");
                                getAllProduct();
                                dialogInsertProduct.dismiss();

                            }else {
                                showToast("err", response.body().getMessage());

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            showProgressBar("s", "S", false);
                            showToast("err", "Tidak ada koneksi internet");



                        }
                    });
                }
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


    private void getCategoris() {
        showProgressBar("Loading", "Memuat data categories...", true);
        adminService.getCategories().enqueue(new Callback<List<CategoriesModel>>() {
            @Override
            public void onResponse(Call<List<CategoriesModel>> call, Response<List<CategoriesModel>> response) {
                showProgressBar("s", "S", false);
                if (response.isSuccessful() && response.body() != null) {
                    categoriesModelList = response.body();
                    spinnerCategoriesAdapter = new SpinnerCategoriesAdapter(getContext(), categoriesModelList);
                    spCategories.setAdapter(spinnerCategoriesAdapter);


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String pdfPath = getRealPathFromUri(uri);
                file = new File(pdfPath);
                etPathImage.setText(file.getName());


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