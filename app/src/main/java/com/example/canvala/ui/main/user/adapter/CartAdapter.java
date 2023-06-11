package com.example.canvala.ui.main.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.canvala.R;
import com.example.canvala.data.model.CartModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Lambda;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    List<CartModel> cartModelList;

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        holder.tvNamaProduct.setText(cartModelList.get(holder.getAdapterPosition()).getProductName());
        holder.tvJumlah.setText("X" + String.valueOf(cartModelList.get(holder.getAdapterPosition()).getQuantity()));

        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        String price = decimalFormat.format(cartModelList.get(holder.getAdapterPosition()).getTotal());
        holder.tvHarga.setText("Rp. " + price);

        Glide.with(context)
                .load(cartModelList.get(holder.getAdapterPosition()).getPhotos())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .centerInside()
                .skipMemoryCache(true)
                .into(holder.ivProduct);

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public void filter(ArrayList<CartModel> filterList) {
        cartModelList = filterList;
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
        }
    }
}
