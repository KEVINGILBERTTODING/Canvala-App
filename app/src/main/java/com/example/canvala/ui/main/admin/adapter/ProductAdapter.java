package com.example.canvala.ui.main.admin.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.canvala.R;
import com.example.canvala.data.api.AdminService;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.ui.main.user.product.DetailProductFragment;
import com.example.canvala.util.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    List<ProductModel> productModelList;
    SharedPreferences sharedPreferences;
    private String userId;
    private AlertDialog progressDialog;
    private UserService userService;
    private AdminService adminService;


    private OnButtonClickListener onButtonClickListener;


    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }


    public ProductAdapter(Context context, List<ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_product_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNamaProduct.setText(productModelList.get(holder.getAdapterPosition()).getProduct_name());
        // Decimal Format rupiah
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        String price = decimalFormat.format(productModelList.get(holder.getAdapterPosition()).getPrice());
        holder.tvHarga.setText("Rp. " + price);

        holder.tvStock.setText(productModelList.get(holder.getAdapterPosition()).getStock() + " Stock");

        Glide.with(context)
                .load(productModelList.get(holder.getAdapterPosition()).getPhotos())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .centerInside()
                .skipMemoryCache(true)
                .into(holder.ivProduct);
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public void filter(ArrayList<ProductModel> filteredList) {
        productModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNamaProduct, tvHarga, tvStock;
        ImageView ivProduct;
        Button btnDelete, btnEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaProduct = itemView.findViewById(R.id.tvNamaProduct);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvStock = itemView.findViewById(R.id.tvStock);
            adminService = ApiConfig.getClient().create(AdminService.class);
            userService = ApiConfig.getClient().create(UserService.class);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Peringatan").setMessage("Apakah anda yakin ingi menghapus data ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showProgressBar("Loading", "Menghapus data...", true);
                                    adminService.deleteProduct(
                                            productModelList.get(getAdapterPosition()).getProduct_id()
                                    ).enqueue(new Callback<ResponseModel>() {
                                        @Override
                                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                                            showProgressBar("s", "s", false);
                                            if (response.isSuccessful() && response.body().getStatus() == 200) {
                                                productModelList.remove(getAdapterPosition());
                                                notifyDataSetChanged();
                                                showToast("success", "Berhasil menghapus produk");
                                            }else {
                                                showToast("err", "Terjadi kesalahan");
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                                            showProgressBar("s", "s", false);
                                            showToast("err", "Tidak ada koneksi internet");

                                        }
                                    });

                                }
                            }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showProgressBar("s", "s", false);
                                    showToast("err", "Terjadi kesalahan");



                                }

                            });

                    alert.show();


                }
            });




        }

        @Override
        public void onClick(View v) {

            Fragment fragment = new DetailProductFragment();
            Bundle bundle = new Bundle();
            bundle.putString("image", productModelList.get(getAdapterPosition()).getPhotos());
            bundle.putString("product_id", productModelList.get(getAdapterPosition()).getId_product());
            bundle.putString("nama_produk", productModelList.get(getAdapterPosition()).getProduct_name());
            bundle.putString("harga", tvHarga.getText().toString());
            bundle.putInt("price", productModelList.get(getAdapterPosition()).getPrice());
            bundle.putInt("stock", productModelList.get(getAdapterPosition()).getStock());
            bundle.putString("detail", productModelList.get(getAdapterPosition()).getDescriptions());
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameUsers, fragment).addToBackStack(null).commit();
        }


    }


    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
            Toasty.success(context, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(context, text, Toasty.LENGTH_SHORT).show();
        }
    }

    public interface OnButtonClickListener {
        void onButtonClicked();
    }


}
