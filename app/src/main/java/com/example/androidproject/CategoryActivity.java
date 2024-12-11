package com.example.androidproject;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.DatabaseHelper;
import com.example.androidproject.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private List<Item> items;
    private ItemAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Back Button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the current activity and return to MainActivity
            }
        });

        String categoryName = getIntent().getStringExtra("category_name");
        TextView categoryTitle = findViewById(R.id.categoryTitle);
        categoryTitle.setText(categoryName);

        EditText productNameInput = findViewById(R.id.productName);
        EditText productPriceInput = findViewById(R.id.productPrice);
        Button addButton = findViewById(R.id.addButton);

        RecyclerView recyclerView = findViewById(R.id.itemsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        items = new ArrayList<>();

        // Load items from the database
        loadItemsFromDatabase(categoryName);

        adapter = new ItemAdapter(items);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameInput.getText().toString().trim();
                String productPriceText = productPriceInput.getText().toString().trim();

                if (!productName.isEmpty() && !productPriceText.isEmpty()) {
                    try {
                        double productPrice = Double.parseDouble(productPriceText);
                        long result = dbHelper.addItem(productName, productPrice, categoryName);

                        if (result != -1) {
                            items.add(new Item(productName, productPrice));
                            adapter.notifyDataSetChanged();

                            productNameInput.setText("");
                            productPriceInput.setText("");

                            Toast.makeText(CategoryActivity.this, "Item added!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CategoryActivity.this, "Failed to save item.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(CategoryActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CategoryActivity.this, "Please enter both name and price", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadItemsFromDatabase(String categoryName) {
        Cursor cursor = dbHelper.getItemsByCategory(categoryName);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
                double itemPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("item_price"));
                items.add(new Item(itemName, itemPrice));
            }
            cursor.close();
        }
    }
}
