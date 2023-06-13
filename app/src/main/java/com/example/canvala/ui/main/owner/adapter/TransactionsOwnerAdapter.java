package com.example.canvala.ui.main.owner.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvala.R;
import com.example.canvala.data.model.TransactionsModel;
import com.example.canvala.ui.main.admin.transactions.DetailTransactionsAdminFragment;
import com.example.canvala.ui.main.owner.transactions.DetailTransactionsOwnerFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionsOwnerAdapter extends RecyclerView.Adapter<TransactionsOwnerAdapter.ViewHolder>  {
    Context context;
    List<TransactionsModel> transactionsModelList;

    public TransactionsOwnerAdapter(Context context, List<TransactionsModel> transactionsModelList) {
        this.context = context;
        this.transactionsModelList = transactionsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.list_transactions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDate.setText(transactionsModelList.get(holder.getAdapterPosition()).getCreatedAt());
        holder.tvStatus.setText(transactionsModelList.get(holder.getAdapterPosition()).getTransactionStatus());
        holder.tvCodeTrans.setText(transactionsModelList.get(holder.getAdapterPosition()).getCode());

        if (transactionsModelList.get(holder.getAdapterPosition()).getTransactionStatus().equals("TERKONFIRMASI")) {
            holder.tvStatus.setTextColor(context.getColor(R.color.yellow));
        }else  if (transactionsModelList.get(holder.getAdapterPosition()).getTransactionStatus().equals("BELUM TERKONFIRMASI")) {
            holder.tvStatus.setTextColor(context.getColor(R.color.red));
        }else  if (transactionsModelList.get(holder.getAdapterPosition()).getTransactionStatus().equals("TERKIRIM")) {
            holder.tvStatus.setTextColor(context.getColor(R.color.main));
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        String price = decimalFormat.format(Integer.parseInt(transactionsModelList.get(holder.getAdapterPosition()).getTotalPrice()));
        holder.tvPrice.setText("Rp. " + price);


    }

    @Override
    public int getItemCount() {
        return transactionsModelList.size();
    }

    public void filter(ArrayList<TransactionsModel> filteredList){
        transactionsModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCodeTrans , tvStatus, tvPrice, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodeTrans = itemView.findViewById(R.id.tvCodeTransactions);
            tvStatus  = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPrice = itemView.findViewById(R.id.tvTotal);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Fragment fragment = new DetailTransactionsOwnerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("transaction_id", transactionsModelList.get(getAdapterPosition()).getTransactionId());
            bundle.putString("code_transactions", transactionsModelList.get(getAdapterPosition()).getCode());
            bundle.putString("bukti_tf", transactionsModelList.get(getAdapterPosition()).getPhotoTransaction());
            bundle.putString("kota", transactionsModelList.get(getAdapterPosition()).getCity());
            bundle.putString("telepon", transactionsModelList.get(getAdapterPosition()).getPhoneNumber());
            bundle.putString("kode_pos", transactionsModelList.get(getAdapterPosition()).getPostalCode());
            bundle.putString("alamat", transactionsModelList.get(getAdapterPosition()).getAddress());
            bundle.putString("nama_lengkap", transactionsModelList.get(getAdapterPosition()).getName());
            bundle.putString("tgl_terima", transactionsModelList.get(getAdapterPosition()).getTimeArrived());
            bundle.putString("nama_penerima", transactionsModelList.get(getAdapterPosition()).getReceiver());
            bundle.putString("nama_bank", transactionsModelList.get(getAdapterPosition()).getBankName());
            bundle.putString("no_rek", transactionsModelList.get(getAdapterPosition()).getNoRek());
            bundle.putString("rekening_name", transactionsModelList.get(getAdapterPosition()).getRekName());
            bundle.putString("berat_total", transactionsModelList.get(getAdapterPosition()).getWeightTotal());
            bundle.putString("total", transactionsModelList.get(getAdapterPosition()).getTotalPrice());
            bundle.putString("status", transactionsModelList.get(getAdapterPosition()).getTransactionStatus());
            fragment.setArguments(bundle);
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameOwner, fragment).addToBackStack(null).commit();
        }
    }
}
