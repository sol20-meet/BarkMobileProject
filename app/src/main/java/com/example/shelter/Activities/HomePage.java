package com.example.shelter.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.example.shelter.Fragments.FragmentAccount;
import com.example.shelter.Fragments.FragmentAdopt;
import com.example.shelter.Fragments.FragmentHome;
import com.example.shelter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class HomePage extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    ChipNavigationBar bottomNav;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        bottomNav = findViewById(R.id.main_nav);

        if (savedInstanceState == null) {
            bottomNav.setItemSelected(R.id.nav_home, true);
            fragmentManager = getSupportFragmentManager();
            FragmentHome fragmentHome = new FragmentHome();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_cont, fragmentHome)
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch (id) {
                    case R.id.nav_home:
                        fragment = new FragmentHome();
                        break;
                    case R.id.nav_dogs:
                        fragment = new FragmentAdopt();
                        break;
                    case R.id.nav_account:
                        fragment = new FragmentAccount();
                        break;
                }
                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_cont, fragment)
                            .commit();
                } else {
                    Log.e(TAG, "Error in creating Fragment!");
                }
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}