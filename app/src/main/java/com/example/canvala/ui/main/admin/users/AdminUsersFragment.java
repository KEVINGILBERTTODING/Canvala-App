package com.example.canvala.ui.main.admin.users;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.canvala.R;
import com.example.canvala.data.api.AdminService;
import com.example.canvala.data.api.ApiConfig;
import com.example.canvala.data.model.UserModel;
import com.example.canvala.data.model.ResponseModel;
import com.example.canvala.data.model.UserModel;
import com.example.canvala.databinding.FragmentRekeningAdminBinding;
import com.example.canvala.databinding.FragmentUsersAdminBinding;
import com.example.canvala.ui.main.admin.adapter.RekeningAdapter;
import com.example.canvala.ui.main.admin.adapter.UsersAdapter;
import com.example.canvala.util.Constants;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class AdminUsersFragment extends Fragment {

    LinearLayoutManager linearLayoutManager;
    UsersAdapter usersAdapter;
    private List<UserModel> userModelList;
    SharedPreferences sharedPreferences;
    AlertDialog progressDialog;
    AdminService adminService;
    String userId, role;


    private FragmentUsersAdminBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsersAdminBinding.inflate(inflater, container, false);
        sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null);
        adminService = ApiConfig.getClient().create(AdminService.class);



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllUsers();
        listener();

    }

    private void listener() {

        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUsers();

            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


    }

    private void getAllUsers() {
        showProgressBar("Loading", "Memuat data categories", true);
        adminService.getAllUsers().enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful() && response.body().size() > 0) {
                    userModelList = response.body();
                    binding.tvEmpty.setVisibility(View.GONE);

                    usersAdapter = new UsersAdapter(getContext(), userModelList);
                    linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvUser.setLayoutManager(linearLayoutManager);
                    binding.rvUser.setAdapter(usersAdapter);
                    binding.rvUser.setHasFixedSize(true);
                    showProgressBar("sd", "dssd", false);
                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    showProgressBar("adss", "sdsd", false);
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                showProgressBar("adss", "sdsd", false);
                showToast("error", "Tidak ada koneksi internet");

            }
        });
    }

    private void insertUsers() {
        Dialog dialog = new Dialog(getContext());
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
        ArrayAdapter adapterRole = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, opsiRole);
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapterRole);
        dialog.show();

        spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                role = opsiRole[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



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
                }else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Tidak boleh kosong");
                    etPassword.requestFocus();
                }else if (etALamat.getText().toString().isEmpty()) {
                    etALamat.setError("Tidak boleh kosong");
                    etALamat.requestFocus();
                } else {
                    showProgressBar("Loading", "Menambahkan kategori baru", true);
                    adminService.insertUsers(
                            etNamaLengkap.getText().toString(),
                            etEmail.getText().toString(), etPassword.getText().toString(), etALamat.getText().toString(),
                                    etTelp.getText().toString(), etKodePos.getText().toString(), role

                            )
                            .enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    showProgressBar("s", "s", false);
                                    if (response.isSuccessful() && response.body().getStatus() == 200) {
                                        showToast("success", "Berhasil menambahkan user baru");
                                        getAllUsers();
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


    private void showProgressBar(String title, String message, boolean isLoading) {
        if (isLoading) {
            // Membuat progress dialog baru jika belum ada
            if (progressDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
            Toasty.success(getContext(), text, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), text, Toasty.LENGTH_SHORT).show();
        }
    }

    private void filter(String text) {
        ArrayList<UserModel> filteredList = new ArrayList<>();
        for (UserModel item : userModelList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

            usersAdapter.filter(filteredList);
            if (filteredList.isEmpty()) {

            }else {
                usersAdapter.filter(filteredList);
            }
        }
    }
}