package com.example.canvala.ui.main.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.canvala.R;
import com.example.canvala.ui.main.user.home.UserHomeFragment;
import com.example.canvala.ui.main.user.product.AllProductFragment;
import com.example.canvala.ui.main.user.transactions.TransactionsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        bottomNavigationView = findViewById(R.id.btmBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    replace(new UserHomeFragment());
                    return true;
                }else if (item.getItemId() == R.id.menuTransaksi) {
                    replace(new TransactionsFragment());
                    return true;
                }else if (item.getItemId() == R.id.menuProduct) {
                    replace(new AllProductFragment());
                    return true;
                }

                return false;
            }
        });

        replace(new UserHomeFragment());
    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameUsers, fragment).commit();
    }
}