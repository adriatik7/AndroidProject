package com.example.androidproject;

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
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ITEMS = "items";

    // Columns for the Users table
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Columns for the Items table
    private static final String COLUMN_ITEM_ID = "id";
    private static final String COLUMN_ITEM_NAME = "item_name";
    private static final String COLUMN_ITEM_PRICE = "item_price";
    private static final String COLUMN_ITEM_CATEGORY = "item_category";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FULL_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUserTable);

        // Create Items table
        String createItemsTable = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_NAME + " TEXT, " +
                COLUMN_ITEM_PRICE + " REAL, " +
                COLUMN_ITEM_CATEGORY + " TEXT)";
        db.execSQL(createItemsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // Add a user
    public boolean addUser(String fullName, String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Hash the password
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
            return null; // In case of an error
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





    // Authenticate user
    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch the stored hashed password for the username
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_PASSWORD},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        if (cursor.moveToFirst()) {
            // Get the stored hashed password
            String storedHashedPassword = cursor.getString(0);

            // Hash the entered password
            String enteredHashedPassword = hashPassword(password);

            cursor.close();

            // Compare the stored hash with the hash of the entered password
            return storedHashedPassword.equals(enteredHashedPassword);
        }

        cursor.close();
        return false; // Authentication failed
    }


    // Add an item
    public long addItem(String itemName, double price, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, itemName);
        values.put(COLUMN_ITEM_PRICE, price);
        values.put(COLUMN_ITEM_CATEGORY, category);

        return db.insert(TABLE_ITEMS, null, values);
    }

    // Get items by category
    public Cursor getItemsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ITEMS,
                null,
                COLUMN_ITEM_CATEGORY + "=?",
                new String[]{category},
                null, null, null);
    }
}
