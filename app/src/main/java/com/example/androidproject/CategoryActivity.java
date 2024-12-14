package com.example.androidproject;

import android.content.Intent;
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


        int userId = getIntent().getIntExtra("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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


        loadItemsFromDatabase(categoryName, userId);

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
                        long result = dbHelper.addItem(productName, productPrice, categoryName, userId);

                        if (result != -1) {
                            items.add(new Item(productName, productPrice, userId));
                            adapter.notifyDataSetChanged();

                            productNameInput.setText("");
                            productPriceInput.setText("");

                            Toast.makeText(CategoryActivity.this, "Item added!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CategoryActivity.this, "Error adding item.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(CategoryActivity.this, "Invalid price format.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CategoryActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void loadItemsFromDatabase(String categoryName, int userId) {
        Cursor cursor = dbHelper.getItemsByUserAndCategory(userId, categoryName);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
                double itemPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("item_price"));
                items.add(new Item(itemName, itemPrice, userId));
            }
            cursor.close();
        }
    }

}
