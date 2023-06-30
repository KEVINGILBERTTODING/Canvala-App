package com.example.canvala.ui.main.admin.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvala.R;
import com.example.canvala.data.api.AdminService;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.data.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    Context context;
    List<UserModel> userModelList;
    private AlertDialog progressDialog;
    private AdminService adminService;
    private String role;

    public UsersAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        holder.tvNama.setText(userModelList.get(holder.getAdapterPosition()).getName());
        holder.tvNoTelp.setText(userModelList.get(holder.getAdapterPosition()).getPhoneNumber());

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public void filter(ArrayList<UserModel> filteredList) {
        userModelList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNoTelp;
        ImageButton btnDelete, btnEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tvNamaUsers);
            tvNoTelp = itemView.findViewById(R.id.tvNoTelp);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            adminService = ApiConfig.getClient().create(AdminService.class);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Peringatan").setMessage("Apakah anda yakin ingin menghapus user ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showProgressBar("Loading", "Menghapus data...", true);
                                    adminService.deleteUser(userModelList.get(getAdapterPosition()).getUserId())
                                            .enqueue(new Callback<ResponseModel>() {
                                                @Override
                                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                    showProgressBar("s", "S", false);
                                                    if (response.isSuccessful() && response.body().getStatus() == 200) {
                                                        userModelList.remove(getAdapterPosition());
                                                        notifyDataSetChanged();
                                                        showToast("success", "Berhasil menghapus user");

                                                    }else {
                                                        showToast("err", "Terjadi kesalahan");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                                    showProgressBar("s", "S", false);
                                                    showToast("err", "Tidak ada koneksi internet");



                                                }
                                            });


                                }
                            }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    alert.show();
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.layout_insert_user);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    Button btnBatal, btnSimpan;
                    EditText etNamaLengkap, etEmail, etTelp, etKodePos, etPassword, etALamat;
                    Spinner spRole;
                    etNamaLengkap = dialog.findViewById(R.id.etNamaLengkap);
                    etEmail = dialog.findViewById(R.id.etEmail);
                    etTelp = dialog.findViewById(R.id.etTelepon);
                    etKodePos = dialog.findViewById(R.id.etKodePos);
                    etPassword = dialog.findViewById(R.id.etPassword);
                    etALamat = dialog.findViewById(R.id.etAlamat);
                    spRole = dialog.findViewById(R.id.spRole);
                    btnSimpan = dialog.findViewById(R.id.btnSimpan);
                    btnBatal = dialog.findViewById(R.id.btnBatal);

                    String [] opsiRole = {"ADMIN", "OWNER", "USER"};
                    ArrayAdapter adapterRole = new ArrayAdapter(context, android.R.layout.simple_spinner_item, opsiRole);
                    adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spRole.setAdapter(adapterRole);


                    etNamaLengkap.setText(userModelList.get(getAdapterPosition()).getName());
                    etEmail.setText(userModelList.get(getAdapterPosition()).getEmail());
                    etTelp.setText(userModelList.get(getAdapterPosition()).getPhoneNumber());
                    etKodePos.setText(userModelList.get(getAdapterPosition()).getPostalCode());
                    etALamat.setText(userModelList.get(getAdapterPosition()).getAddress());
                    spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            role = opsiRole[position];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    dialog.show();



                    btnBatal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnSimpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etNamaLengkap.getText().toString().isEmpty()) {
                                etNamaLengkap.setError("Tidak boleh kosong");
                                etNamaLengkap.requestFocus();
                            }else if (etEmail.getText().toString().isEmpty()) {
                                etEmail.setError("Tidak boleh kosong");
                                etEmail.requestFocus();
                            }else if (etTelp.getText().toString().isEmpty()) {
                                etTelp.setError("Tidak boleh kosong");
                                etTelp.requestFocus();
                            }else if (etKodePos.getText().toString().isEmpty()) {
                                etKodePos.setError("Tidak boleh kosong");
                                etKodePos.requestFocus();
                            }else if (etALamat.getText().toString().isEmpty()) {
                                etALamat.setError("Tidak boleh kosong");
                                etALamat.requestFocus();
                            } else {
                                showProgressBar("Loading", "Menambahkan kategori baru", true);
                                adminService.updateUsers(
                                        userModelList.get(getAdapterPosition()).getUserId(),
                                                etNamaLengkap.getText().toString(),
                                                etEmail.getText().toString(), etPassword.getText().toString(), etALamat.getText().toString(),
                                                etTelp.getText().toString(), etKodePos.getText().toString(), role

                                        )
                                        .enqueue(new Callback<ResponseModel>() {
                                            @Override
                                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                showProgressBar("s", "s", false);
                                                if (response.isSuccessful() && response.body().getStatus() == 200) {
                                                    showToast("success", "Berhasil mengubah user");
                                                    userModelList.get(getAdapterPosition()).setName(etNamaLengkap.getText().toString());
                                                    userModelList.get(getAdapterPosition()).setEmail(etEmail.getText().toString());
                                                    userModelList.get(getAdapterPosition()).setAddress(etALamat.getText().toString());
                                                    userModelList.get(getAdapterPosition()).setPhoneNumber(etTelp.getText().toString());
                                                    userModelList.get(getAdapterPosition()).setPostalCode(etKodePos.getText().toString());
                                                    notifyDataSetChanged();

                                                    dialog.dismiss();

                                                }else {
                                                    showToast("error", "Terjadi keslaahan");

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                                showProgressBar("s", "s", false);
                                                showToast("error", "Tidak ada koneksi internet");



                                            }
                                        });
                            }
                        }
                    });
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
        if (jenis.equals("succ")) {
            Toasty.success(context, text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(context, text, Toasty.LENGTH_SHORT).show();
        }
    }
}
