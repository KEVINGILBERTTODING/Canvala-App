package com.example.canvala.ui.main.user.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.ui.main.user.home.UserHomeFragment;
import com.example.canvala.ui.main.user.product.DetailProductFragment;
import com.example.canvala.util.Constants;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;



public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    List<ProductModel> productModelList;
    SharedPreferences sharedPreferences;
    private String userId;
    private AlertDialog progressDialog;
    private UserService userService;



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
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
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
        Button btnCart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaProduct = itemView.findViewById(R.id.tvNamaProduct);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvStock = itemView.findViewById(R.id.tvStock);
            btnCart = itemView.findViewById(R.id.btnCart);
            itemView.setOnClickListener(this);

            userService = ApiConfig.getClient().create(UserService.class);

            sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);



            btnCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.layout_add_cart);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    ImageView ivProduct = dialog.findViewById(R.id.ivProduct);
                    TextView tvNamaProduct = dialog.findViewById(R.id.tvNamaProduct);
                    TextView tvprice = dialog.findViewById(R.id.tvPrice);
                    EditText etQty = dialog.findViewById(R.id.etQty);
                    Button btnMasukkan = dialog.findViewById(R.id.btnMasukkan);

                    etQty.requestFocus();


                    Glide.with(context)
                            .load(productModelList.get(getAdapterPosition()).getPhotos())
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .fitCenter()
                            .centerInside()
                            .skipMemoryCache(true)
                            .into(ivProduct);

                    DecimalFormat decimalFormat = new DecimalFormat("#,##0");
                    String price = decimalFormat.format(productModelList.get(getAdapterPosition()).getPrice());
                    tvprice.setText("Rp. " + price);
                    tvNamaProduct.setText(productModelList.get(getAdapterPosition()).getProduct_name());

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
                               int price = productModelList.get(getAdapterPosition()).getPrice();
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
                                    productModelList.get(getAdapterPosition()).getProduct_id()
                            ).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                                    if (response.isSuccessful() && response.body().getStatus() == 200) {
                                        showProgressBar("dssd", "sds", false);
                                        Integer stockSesudah = Integer.parseInt(etQty.getText().toString());
                                        Integer stokSebelum = productModelList.get(getAdapterPosition()).getStock();
                                        Integer sisaSaldo = stokSebelum - stockSesudah;
                                        Log.d("sisa saldo", "onResponse: " + sisaSaldo);
                                        productModelList.get(getAdapterPosition()).setStock(sisaSaldo);
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                        if (onButtonClickListener != null) {
                                            onButtonClickListener.onButtonClicked();
                                        }
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
