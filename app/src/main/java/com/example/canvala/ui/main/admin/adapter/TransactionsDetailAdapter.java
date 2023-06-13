package com.example.canvala.ui.main.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.canvala.R;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.api.UserService;
import com.example.canvala.data.model.TransactionsDetailModel;
import com.example.canvala.data.model.ResponseModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsDetailAdapter extends RecyclerView.Adapter<TransactionsDetailAdapter.ViewHolder> {
    Context context;
    List<TransactionsDetailModel> detailTransactions;
    AlertDialog progressDialog;
    UserService userService;

    private TransactionsDetailAdapter.OnButtonClickListener onButtonClickListener;


    public void setOnButtonClickListener(TransactionsDetailAdapter.OnButtonClickListener listener) {
        this.onButtonClickListener = listener;
    }


    public TransactionsDetailAdapter(Context context, List<TransactionsDetailModel> detailTransactions) {
        this.context = context;
        this.detailTransactions = detailTransactions;
    }

    @NonNull
    @Override
    public TransactionsDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_detail_transactions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsDetailAdapter.ViewHolder holder, int position) {
        holder.tvNamaProduct.setText(detailTransactions.get(holder.getAdapterPosition()).getProductName());
        holder.tvJumlah.setText("X" + String.valueOf(detailTransactions.get(holder.getAdapterPosition()).getBanyak()));

        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        String price = decimalFormat.format(Integer.parseInt(String.valueOf(detailTransactions.get(holder.getAdapterPosition()).getPrice())));
        holder.tvHarga.setText("Rp. " + price);

        Glide.with(context)
                .load(detailTransactions.get(holder.getAdapterPosition()).getPhotos())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .centerInside()
                .skipMemoryCache(true)
                .into(holder.ivProduct);

    }

    @Override
    public int getItemCount() {
        return detailTransactions.size();
    }

    public void filter(ArrayList<TransactionsDetailModel> filterList) {
        detailTransactions = filterList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaProduct, tvJumlah, tvHarga;
        ImageView ivProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvNamaProduct = itemView.findViewById(R.id.tvNamaProduct);
            tvJumlah = itemView.findViewById(R.id.tvJumlah);
            tvHarga = itemView.findViewById(R.id.tvPrice);
            userService = ApiConfig.getClient().create(UserService.class);

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
