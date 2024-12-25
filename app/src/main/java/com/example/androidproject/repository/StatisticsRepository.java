package com.example.androidproject.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidproject.database.DatabaseHelper;

public class StatisticsRepository {

    private DatabaseHelper dbHelper;

    public StatisticsRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public Cursor getTop5HighestPricedItems(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT * FROM items WHERE user_id = ? ORDER BY item_price DESC LIMIT 5";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public double getTotalSpent(int userId) {
        SQLiteDatabase db = null;
        double totalSpent = 0;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT SUM(item_price) AS total FROM items WHERE user_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            if (cursor.moveToFirst()) {
                totalSpent = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return totalSpent;
    }

    public Cursor getTotalPriceByCategory(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String query = "SELECT item_category, SUM(item_price) AS total FROM items WHERE user_id = ? GROUP BY item_category ORDER BY total DESC";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursor;
    }
}
