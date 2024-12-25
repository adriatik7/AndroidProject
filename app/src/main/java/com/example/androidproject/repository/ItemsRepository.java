package com.example.androidproject.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidproject.database.DatabaseHelper;

public class ItemsRepository {

    private DatabaseHelper dbHelper;

    public ItemsRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addItem(String itemName, double price, String category, int userId) {
        SQLiteDatabase db = null;
        long result = -1;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("item_name", itemName);
            values.put("item_price", price);
            values.put("item_category", category);
            values.put("user_id", userId);

            result = db.insert("items", null, values);
            if (result == -1) {
                throw new SQLException("Failed to insert item");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    public Cursor getItemsByUserAndCategory(int userId, String category) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query("items",
                    null,
                    "user_id = ? AND item_category = ?",
                    new String[]{String.valueOf(userId), category},
                    null, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public int updateItem(int itemId, String newName, double newPrice, String newCategory) {
        SQLiteDatabase db = null;
        int rowsAffected = 0;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("item_name", newName);
            values.put("item_price", newPrice);
            values.put("item_category", newCategory);

            rowsAffected = db.update("items", values, "id = ?", new String[]{String.valueOf(itemId)});
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update item with id: " + itemId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return rowsAffected;
    }

    public int deleteItem(int itemId) {
        SQLiteDatabase db = null;
        int rowsAffected = 0;
        try {
            db = dbHelper.getWritableDatabase();
            rowsAffected = db.delete("items", "id = ?", new String[]{String.valueOf(itemId)});
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete item with id: " + itemId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return rowsAffected;
    }
}
