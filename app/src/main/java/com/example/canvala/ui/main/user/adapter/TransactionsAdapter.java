package com.example.canvala.ui.main.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvala.R;
import com.example.canvala.data.model.TransactionsModel;

import java.text.DecimalFormat;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder>  {
    Context context;
    List<TransactionsModel> transactionsModelList;

    public TransactionsAdapter(Context context, List<TransactionsModel> transactionsModelList) {
        this.context = context;
        this.transactionsModelList = transactionsModelList;
    }

    @NonNull
    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.list_transactions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder holder, int position) {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodeTrans , tvStatus, tvPrice, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodeTrans = itemView.findViewById(R.id.tvCodeTransactions);
            tvStatus  = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPrice = itemView.findViewById(R.id.tvTotal);
        }
    }
}
