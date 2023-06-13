package com.example.canvala.ui.main.owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.canvala.R;
import com.example.canvala.ui.main.admin.home.AdminHomeFragment;
import com.example.canvala.ui.main.owner.home.OwnerHomeFragment;
import com.example.canvala.ui.main.user.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class OwnerMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        bottomNavigationView = findViewById(R.id.btmBar);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    replace(new OwnerHomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.menuProfile) {
                    replace(new ProfileFragment());
                    return true;
                }

                return false;
            }
        });

        replace(new OwnerHomeFragment());
    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameOwner, fragment).commit();
    }
}