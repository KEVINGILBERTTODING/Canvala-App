package com.example.canvala.ui.main.user.product;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.databinding.FragmentDetailProductBinding;
import com.example.canvala.util.Constants;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailProductFragment extends Fragment {
    private FragmentDetailProductBinding binding;
    private String productId;
    AlertDialog progressDialog;
    Integer price;
    UserService userService;
    SharedPreferences sharedPreferences;
    String userId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailProductBinding.inflate(inflater, container, false);
        productId = getArguments().getString("product_id");
        price = getArguments().getInt("price");
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userService = ApiConfig.getClient().create(UserService.class);
        userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);

        Glide.with(getContext())
                .load(getArguments().getString("image"))
                .skipMemoryCache(true)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.ivProduct);


        String replacedString = getArguments().getString("detail").replaceAll("<p>|</p>|&nbsp;|\n", "");


        binding.tvNamaProduct.setText(getArguments().getString("nama_produk"));
        binding.tvHarga.setText(getArguments().getString("harga"));
        binding.tvDetailProduct.setText(replacedString);
        binding.tvStok.setText(getArguments().getInt("stock") + " Stock");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
    }

    private void listener() {

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.layout_add_cart);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ImageView ivProduct = dialog.findViewById(R.id.ivProduct);
                TextView tvNamaProduct = dialog.findViewById(R.id.tvNamaProduct);
                TextView tvprice = dialog.findViewById(R.id.tvPrice);
                EditText etQty = dialog.findViewById(R.id.etQty);
                Button btnMasukkan = dialog.findViewById(R.id.btnMasukkan);

                etQty.requestFocus();


                Glide.with(getContext())
                        .load(getArguments().getString("image"))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .fitCenter()
                        .centerInside()
                        .skipMemoryCache(true)
                        .into(ivProduct);

                DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                String price = decimalFormat.format(getArguments().getInt("price"));
                tvprice.setText("Rp. " + price);
                tvNamaProduct.setText(getArguments().getString("nama_produk"));

                dialog.show();

                etQty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (etQty.getText().toString().equals("0")) {
                            btnMasukkan.setEnabled(false);
                            showToast("error", "Jumlah tidak valid");

                        }else if (etQty.getText().toString().isEmpty()) {
                            btnMasukkan.setEnabled(false);
                        }else if (Integer.parseInt(etQty.getText().toString()) < 1) {
                            showToast("error", "Jumlah tidak valid");
                            btnMasukkan.setEnabled(false);
                        }

                        else {
                            // kalikan qty dengan harga
                            int qty = Integer.parseInt(etQty.getText().toString());
                            int price = getArguments().getInt("price");
                            int total = qty * price;
                            tvprice.setText("Rp. " + decimalFormat.format(total));
                            btnMasukkan.setEnabled(true);
                        }


                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                btnMasukkan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showProgressBar("Loading", "Memeriksa stok product...", true);
                        userService.addProductToCart(
                                Integer.parseInt(etQty.getText().toString()),
                                userId,
                                getArguments().getString("product_id")
                        ).enqueue(new Callback<ResponseModel>() {
                            @Override
                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                                if (response.isSuccessful() && response.body().getStatus() == 200) {
                                    showProgressBar("dssd", "sds", false);


                                    dialog.dismiss();

                                    showToast("success", "Produk berhasil ditambahkan");
                                }else {
                                    showToast("error", response.body().getMessage());
                                    showProgressBar("sdd", "Sdsd", false);
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                showToast("error", "Tidak ada koneksi internet");
                                showProgressBar("sdd", "Sdsd", false);

                            }
                        });
                    }
                });





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