package com.example.androidproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SpendTracker.db";
    private static final int DATABASE_VERSION = 2;


    private static final String TABLE_USERS = "users";
    private static final String TABLE_ITEMS = "items";


    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";


    private static final String COLUMN_ITEM_ID = "id";
    private static final String COLUMN_ITEM_NAME = "item_name";
    private static final String COLUMN_ITEM_PRICE = "item_price";
    private static final String COLUMN_ITEM_CATEGORY = "item_category";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULL_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);


        String createItemsTable = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                COLUMN_ITEM_PRICE + " REAL NOT NULL, " +
                COLUMN_ITEM_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ") " +
                "ON DELETE CASCADE)";
        db.execSQL(createItemsTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE items ADD COLUMN user_id INTEGER DEFAULT 0;");
        }
    }



    public boolean addUser(String fullName, String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        String hashedPassword = hashPassword(password);

        values.put(COLUMN_FULL_NAME, fullName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, hashedPassword);

        try {
            long result = db.insert(TABLE_USERS, null, values);
            if (result == -1) {
                Log.e("DatabaseHelper", "Error inserting user");
                return false;
            }
            return true;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting user: " + e.getMessage());
            return false;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b)); // Convert bytes to hex
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }






    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_PASSWORD},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        if (cursor.moveToFirst()) {

            String storedHashedPassword = cursor.getString(0);


            String enteredHashedPassword = hashPassword(password);

            cursor.close();


            return storedHashedPassword.equals(enteredHashedPassword);
        }

        cursor.close();
        return false;
    }



    public long addItem(String itemName, double price, String category, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_ITEM_PRICE, price);
        values.put(COLUMN_ITEM_CATEGORY, category);
        values.put("user_id", userId);

        return db.insert(TABLE_ITEMS, null, values);
    }



    public Cursor getItemsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ITEMS,
                null,
                COLUMN_ITEM_CATEGORY + "=?",
                new String[]{category},
                null, null, null);
    }

    public Cursor getItemsByUserAndCategory(int userId, String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ITEMS,
                null,
                "user_id=? AND item_category=?",
                new String[]{String.valueOf(userId), category},
                null, null, null);
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            cursor.close();
            return userId;
        }

        cursor.close();
        return -1;
    }

    public double getTotalSpent(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalSpent = 0;

        String query = "SELECT SUM(" + COLUMN_ITEM_PRICE + ") AS total FROM " + TABLE_ITEMS +
                " WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            totalSpent = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        }
        cursor.close();
        return totalSpent;
    }


    public int updateItem(int itemId, String newName, double newPrice, String newCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, newName);
        values.put(COLUMN_ITEM_PRICE, newPrice);
        values.put(COLUMN_ITEM_CATEGORY, newCategory);

        return db.update(TABLE_ITEMS, values, COLUMN_ITEM_ID + "=?", new String[]{String.valueOf(itemId)});
    }


    public int deleteItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ITEMS, COLUMN_ITEM_ID + "=?", new String[]{String.valueOf(itemId)});
    }

    public boolean isEmailRegistered(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }


    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, hashPassword(newPassword));

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
        return rowsAffected > 0;
    }

    public boolean changePassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, hashPassword(newPassword));

        // Correct method call
        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
        return rowsAffected > 0;
    }


    public boolean updateUsername(int userId, String newUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, newUsername);

        try {
            int rowsAffected = db.update(TABLE_USERS, values, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error updating username: " + e.getMessage());
            return false;
        }
    }


    public Cursor getTop5HighestPricedItems(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEMS +
                " WHERE user_id = ? " +
                " ORDER BY " + COLUMN_ITEM_PRICE + " DESC LIMIT 5";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    public Cursor rankCategoriesByTotalPrice(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_ITEM_CATEGORY + ", SUM(" + COLUMN_ITEM_PRICE + ") AS total_price " +
                "FROM " + TABLE_ITEMS +
                " WHERE user_id = ? " +
                "GROUP BY " + COLUMN_ITEM_CATEGORY +
                " ORDER BY total_price DESC";

        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    public Cursor getTotalPriceByCategory(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ITEM_CATEGORY + ", SUM(" + COLUMN_ITEM_PRICE + ") AS total " +
                "FROM " + TABLE_ITEMS +
                " WHERE user_id = ? " +
                "GROUP BY " + COLUMN_ITEM_CATEGORY +
                " ORDER BY total DESC";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }
}
