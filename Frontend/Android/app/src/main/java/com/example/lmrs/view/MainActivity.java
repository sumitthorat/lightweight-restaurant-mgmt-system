package com.example.lmrs.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.lmrs.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init all the UI elements
        bottomNav = findViewById(R.id.bottom_navigation);

        // Set onClickListener
        bottomNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        // Initialize fragment
        openFragment(EditMenuFragment.newInstance("", ""));


    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment, fragment);
        fragmentTransaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_edit_menu:
                        Log.i(TAG, "Edit menu");
                        openFragment(EditMenuFragment.newInstance("", ""));
                        return true;

                    case R.id.navigation_view_order:
                        Log.i(TAG, "View Order");
                        openFragment(ViewOrdersFragment.newInstance("", ""));
                        return true;

                    case R.id.navigation_statstics:
                        Log.i(TAG, "Statistics");
                        openFragment(StatisticsFragment.newInstance("", ""));
                        return true;
                }
                return false;
            };



}