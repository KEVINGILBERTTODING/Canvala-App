package com.example.canvala.ui.main.admin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvala.R;
import com.example.canvala.data.api.AdminService;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.model.RekeningModel;
import com.example.canvala.data.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekeningAdapter extends RecyclerView.Adapter<RekeningAdapter.ViewHolder> {
    Context context;
    List<RekeningModel> rekeningModelList;
    private AdminService adminService;
    private AlertDialog progressDialog;

    public RekeningAdapter(Context context, List<RekeningModel> rekeningModelList) {
        this.context = context;
        this.rekeningModelList = rekeningModelList;
    }

    @NonNull
    @Override
    public RekeningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_rekening, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RekeningAdapter.ViewHolder holder, int position) {
        holder.tvBankName.setText(rekeningModelList.get(holder.getAdapterPosition()).getBankName());
        holder.tvNorek.setText(rekeningModelList.get(holder.getAdapterPosition()).getNumber());
        holder.tvNama.setText(rekeningModelList.get(holder.getAdapterPosition()).getRekeningName());


    }

    @Override
    public int getItemCount() {
        return rekeningModelList.size();
    }

    public void filter(ArrayList<RekeningModel> filteredList) {
        rekeningModelList = filteredList;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNorek, tvNama, tvBankName;
        ImageView btnDelete, btnEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNorek = itemView.findViewById(R.id.tvNoRek);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvBankName = itemView.findViewById(R.id.tvBankName);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            adminService = ApiConfig.getClient().create(AdminService.class);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Peringatan").setMessage("Apakah anda yakin ingin menghapus data ini?");
                    alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showProgressBar("Loading", "Menghapus data...", true);
                            adminService.deleteRekening(String.valueOf(rekeningModelList.get(getAdapterPosition()).getIdRekening()))
                                    .enqueue(new Callback<ResponseModel>() {
                                        @Override
                                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                            showProgressBar("s", "s", false);
                                            if (response.isSuccessful() && response.body().getStatus() == 200) {
                                                showToast("success", "Berhasil menghapus data");
                                                rekeningModelList.remove(getAdapterPosition());
                                                notifyDataSetChanged();

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
                    })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    alert.show();
                }
            });
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
}
