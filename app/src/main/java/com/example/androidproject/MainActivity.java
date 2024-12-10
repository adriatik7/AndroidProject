package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set click listeners for each category
        setCategoryClickListener(R.id.category1, "Food & Drinks");
        setCategoryClickListener(R.id.category2, "Transportation");
        setCategoryClickListener(R.id.category3, "Shopping");
        setCategoryClickListener(R.id.category4, "Health");
        setCategoryClickListener(R.id.category5, "Bills & Utilities");
        setCategoryClickListener(R.id.category6, "Entertainment");
        setCategoryClickListener(R.id.category7, "Savings & Investments");
        setCategoryClickListener(R.id.category8, "Others");
    }

    private void setCategoryClickListener(int categoryId, String categoryName) {
        ImageView category = findViewById(categoryId);
        category.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            intent.putExtra("category_name", categoryName);
            startActivity(intent);
        });
    }
}
