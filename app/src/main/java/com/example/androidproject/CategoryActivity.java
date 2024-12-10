package com.example.androidproject;

import android.content.Intent;
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

import com.example.androidproject.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private List<Item> items;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Back Button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity and return to MainActivity
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

        items = new ArrayList<>();
        items.add(new Item("Example Item 1", 10.99));
        items.add(new Item("Example Item 2", 5.49));

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
                        items.add(new Item(productName, productPrice));
                        adapter.notifyDataSetChanged();

                        productNameInput.setText("");
                        productPriceInput.setText("");

                        Toast.makeText(CategoryActivity.this, "Item added!", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(CategoryActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CategoryActivity.this, "Please enter both name and price", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
