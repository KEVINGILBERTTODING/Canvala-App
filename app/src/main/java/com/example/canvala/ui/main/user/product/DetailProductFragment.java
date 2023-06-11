package com.example.canvala.ui.main.user.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.canvala.R;
import com.example.canvala.databinding.FragmentDetailProductBinding;

import org.w3c.dom.Text;


public class DetailProductFragment extends Fragment {
    private FragmentDetailProductBinding binding;
    private String productId;
    private Integer price;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailProductBinding.inflate(inflater, container, false);
        productId = getArguments().getString("product_id");
        price = getArguments().getInt("price");

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
    }


}