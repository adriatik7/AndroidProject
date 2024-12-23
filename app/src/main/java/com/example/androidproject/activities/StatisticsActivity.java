package com.example.androidproject.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.androidproject.database.DatabaseHelper;
import com.example.androidproject.R;
import com.example.androidproject.utils.SessionManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private TextView topItemsTextView;
    private BarChart categoryBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        topItemsTextView = findViewById(R.id.top_items_text_view);
        categoryBarChart = findViewById(R.id.category_bar_chart);

        // Get the user ID from the session
        int userId = sessionManager.getUserId();

        if (userId != -1 && sessionManager.isLoggedIn()) {
            displayTop5HighestPricedItems(userId);
            displayCategoryRanking(userId);
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
                } else if (itemId == R.id.nav_profile) {
                    Intent profileIntent = new Intent(StatisticsActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
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

    private void displayCategoryRanking(int userId) {
        Cursor cursor = dbHelper.getTotalPriceByCategory(userId);

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();

        int index = 0;
        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndexOrThrow("item_category"));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));

                entries.add(new BarEntry(index++, (float) total));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();

        BarDataSet dataSet = new BarDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData data = new BarData(dataSet);

        categoryBarChart.setData(data);
        categoryBarChart.getDescription().setEnabled(false);

        XAxis xAxis = categoryBarChart.getXAxis();
        xAxis.setGranularity(1f);

        YAxis yAxisLeft = categoryBarChart.getAxisLeft();
        yAxisLeft.setTextColor(getResources().getColor(android.R.color.white));
        yAxisLeft.setTextSize(12f);

        YAxis yAxisRight = categoryBarChart.getAxisRight();
        yAxisRight.setEnabled(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return categories.get((int) value);
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(android.R.color.white)); // Set X-axis text color to white
        xAxis.setTextSize(12f);

        categoryBarChart.invalidate();
    }
}
