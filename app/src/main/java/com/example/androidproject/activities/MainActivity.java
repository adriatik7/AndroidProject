package com.example.androidproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.database.DatabaseHelper;
import com.example.androidproject.R;
import com.example.androidproject.repository.StatisticsRepository;
import com.example.androidproject.repository.UserRepository;
import com.example.androidproject.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private StatisticsRepository statisticsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        statisticsRepository = new StatisticsRepository(this);


        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        int userId = sessionManager.getUserId();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        double totalSpentValue = statisticsRepository.getTotalSpent(userId);

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


        bottomNavigation.setSelectedItemId(R.id.nav_main);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                Intent mainIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (itemId == R.id.nav_stats) {
                Intent mainIntent = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            } else if (itemId == R.id.nav_main) {
                return true;
            }
            return false;
        });

    }

    private void setCategoryClickListener(int categoryId, String categoryName) {
        ImageView category = findViewById(categoryId);
        category.setOnClickListener(view -> {
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_bounce));
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
        double totalSpentValue = statisticsRepository.getTotalSpent(userId);

        TextView totalSpentTextView = findViewById(R.id.totalSpent);
        totalSpentTextView.setText(String.format("€%.2f", totalSpentValue));
    }


}
