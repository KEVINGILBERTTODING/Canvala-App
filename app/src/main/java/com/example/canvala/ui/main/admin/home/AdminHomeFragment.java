package com.example.canvala.ui.main.admin.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.canvala.R;
import com.example.canvala.databinding.FragmentAdminHomeFragmnetBinding;
import com.example.canvala.ui.main.admin.transactions.TransactionsFragment;

public class AdminHomeFragment extends Fragment {

    private FragmentAdminHomeFragmnetBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentAdminHomeFragmnetBinding.inflate(inflater, container, false);

         return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
    }

    private void listener() {
        binding.cvMenuTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new TransactionsFragment());

            }
        });
    }

    private void replace(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAdmin, fragment)
                .addToBackStack(null).commit();

    }
}