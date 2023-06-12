package com.example.canvala.ui.main.user.transactions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.canvala.R;
import com.example.canvala.databinding.FragmentDetailTransactionsBinding;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class DetailTransactionsFragment extends Fragment {

    private FragmentDetailTransactionsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailTransactionsBinding.inflate(inflater, container, false);

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

        Log.d("bukti tf", "onViewCreated: " + getArguments().getString("bukti_tf"));

        if (getArguments().getString("bukti_tf").equals("")) {
            binding.btnPesan.setVisibility(View.VISIBLE);
        }else {
            binding.btnPesan.setVisibility(View.GONE);

        }


    }

    private void getFormatRupiah(TextView tvText, String number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        String price = decimalFormat.format(Integer.parseInt(number));
        tvText.setText("Rp. " + price);
    }
}