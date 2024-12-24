package com.example.androidproject.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.database.DatabaseHelper;
import com.example.androidproject.adapters.ItemAdapter;
import com.example.androidproject.R;
import com.example.androidproject.utils.SessionManager;
import com.example.androidproject.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private List<Item> items;
    private ItemAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        sessionManager = new SessionManager(this);


        if (!sessionManager.isLoggedIn()) {
            Intent loginIntent = new Intent(CategoryActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        int userId = sessionManager.getUserId();  // Retrieve userId from session

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

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

        adapter = new ItemAdapter(items, new ItemAdapter.OnItemActionListener() {
            @Override
            public void onEditItem(Item item) {
                showEditDialog(item);
            }

            @Override
            public void onDeleteItem(Item item) {
                deleteItem(item);
            }
        });
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            String productName = productNameInput.getText().toString().trim();
            String productPriceText = productPriceInput.getText().toString().trim();

            if (!productName.isEmpty() && !productPriceText.isEmpty()) {
                try {
                    double productPrice = Double.parseDouble(productPriceText);
                    long result = dbHelper.addItem(productName, productPrice, categoryName, userId);

                    if (result != -1) {
                        items.add(new Item((int) result, productName, productPrice, userId, categoryName));
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
        });
    }

    private void loadItemsFromDatabase(String categoryName, int userId) {
        items.clear();
        Cursor cursor = dbHelper.getItemsByUserAndCategory(userId, categoryName);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
                double itemPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("item_price"));
                items.add(new Item(itemId, itemName, itemPrice, userId, categoryName));
            }
            cursor.close();
        }
    }

    private void showEditDialog(Item item) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_item, null);
        EditText editName = dialogView.findViewById(R.id.editItemName);
        EditText editPrice = dialogView.findViewById(R.id.editItemPrice);

        editName.setText(item.getName());
        editPrice.setText(String.valueOf(item.getPrice()));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Item")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = editName.getText().toString().trim();
                    String priceInput = editPrice.getText().toString().trim();

                    if (newName.isEmpty() || priceInput.isEmpty()) {
                        Toast.makeText(this, "Name and price cannot be empty.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double newPrice;
                    try {
                        newPrice = Double.parseDouble(priceInput);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid price format.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dbHelper.updateItem(item.getId(), newName, newPrice, item.getCategory());
                    loadItemsFromDatabase(item.getCategory(), item.getUserId());
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void deleteItem(Item item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteItem(item.getId());
                    items.remove(item);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Item deleted.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .create()
                .show();
    }
}
