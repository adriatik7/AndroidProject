package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int userId = getIntent().getIntExtra("user_id", -1);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        double totalSpentValue = dbHelper.getTotalSpent(userId);

        TextView totalSpentTextView = findViewById(R.id.totalSpent);
        totalSpentTextView.setText(String.format("%.2f €", totalSpentValue));




        setCategoryClickListener(R.id.category1, "Food & Drinks", userId);
        setCategoryClickListener(R.id.category2, "Transportation", userId);
        setCategoryClickListener(R.id.category3, "Shopping", userId);
        setCategoryClickListener(R.id.category4, "Health", userId);
        setCategoryClickListener(R.id.category5, "Bills & Utilities", userId);
        setCategoryClickListener(R.id.category6, "Entertainment", userId);
        setCategoryClickListener(R.id.category7, "Savings & Investments", userId);
        setCategoryClickListener(R.id.category8, "Others", userId);
    }

    private void setCategoryClickListener(int categoryId, String categoryName, int userId) {
        ImageView category = findViewById(categoryId);
        category.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            intent.putExtra("category_name", categoryName);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int userId = getIntent().getIntExtra("user_id", -1);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        double totalSpentValue = dbHelper.getTotalSpent(userId);

        TextView totalSpentTextView = findViewById(R.id.totalSpent);
        totalSpentTextView.setText(String.format("%.2f euro", totalSpentValue));
    }

}
