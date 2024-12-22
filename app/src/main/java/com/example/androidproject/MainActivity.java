package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);


        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        int userId = sessionManager.getUserId();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        double totalSpentValue = dbHelper.getTotalSpent(userId);

        TextView totalSpentTextView = findViewById(R.id.totalSpent);
        totalSpentTextView.setText(String.format("%.2f €", totalSpentValue));

        setCategoryClickListener(R.id.category1, "Food & Drinks");
        setCategoryClickListener(R.id.category2, "Transportation");
        setCategoryClickListener(R.id.category3, "Shopping");
        setCategoryClickListener(R.id.category4, "Health");
        setCategoryClickListener(R.id.category5, "Bills & Utilities");
        setCategoryClickListener(R.id.category6, "Entertainment");
        setCategoryClickListener(R.id.category7, "Savings & Investments");
        setCategoryClickListener(R.id.category8, "Others");


        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_profile);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_profile) {
                    Intent mainIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(mainIntent);
                    return true;
                } else if (itemId == R.id.nav_main) {
                    return true;
                }
                return false;
            }
        });
    }

    private void setCategoryClickListener(int categoryId, String categoryName) {
        ImageView category = findViewById(categoryId);
        category.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            intent.putExtra("category_name", categoryName);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        int userId = sessionManager.getUserId();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        double totalSpentValue = dbHelper.getTotalSpent(userId);

        TextView totalSpentTextView = findViewById(R.id.totalSpent);
        totalSpentTextView.setText(String.format("€%.2f", totalSpentValue));
    }


}
