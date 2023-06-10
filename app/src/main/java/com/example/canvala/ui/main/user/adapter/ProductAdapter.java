package com.example.canvala.ui.main.user.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.canvala.R;
import com.example.canvala.data.model.ProductModel;
import com.example.canvala.ui.main.user.product.DetailProductFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    List<ProductModel> productModelList;

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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaProduct = itemView.findViewById(R.id.tvNamaProduct);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvStock = itemView.findViewById(R.id.tvStock);
            itemView.setOnClickListener(this);
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
            bundle.putString("detail", productModelList.get(getAdapterPosition()).getDescriptions());
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameUsers, fragment).addToBackStack(null).commit();
        }
    }
}
