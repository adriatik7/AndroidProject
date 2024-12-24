package com.example.androidproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private TextView topItemsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        topItemsTextView = findViewById(R.id.top_items_text_view);

        // Get the user ID from the session
        int userId = sessionManager.getUserId();

        if (userId != -1 && sessionManager.isLoggedIn()) {
            displayTop5HighestPricedItems(userId);
        } else {
            topItemsTextView.setText("Error: User not logged in.");
        }

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_main) {
                    Intent mainIntent = new Intent(StatisticsActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    return true;
                } else if (itemId == R.id.nav_profile){
                    Intent mainIntent = new Intent(StatisticsActivity.this, ProfileActivity.class);
                    startActivity(mainIntent);
                    return true;
                } else if (itemId == R.id.nav_stats) {
                    return true;
                }
                return false;
            }
        });
    }

    private void displayTop5HighestPricedItems(int userId) {
        Cursor cursor = dbHelper.getTop5HighestPricedItems(userId);
        StringBuilder topItems = new StringBuilder("Top 5 Highest Priced Items:\n\n");

        if (cursor.moveToFirst()) {
            do {
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
                double itemPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("item_price"));
                topItems.append(itemName).append(" - $").append(itemPrice).append("\n");
            } while (cursor.moveToNext());
        } else {
            topItems.append("No items found.");
        }
        cursor.close();

        topItemsTextView.setText(topItems.toString());
    }
}
