package com.example.androidproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private TextView tvFullName, tvEmail, tvUsername;
    private ImageView ivProfilePicture;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);


        if (!sessionManager.isLoggedIn()) {
            Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvUsername = findViewById(R.id.tvUsername);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DatabaseHelper(this);

        int userId = sessionManager.getUserId();


        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT full_name, email, username FROM users WHERE id = ?",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            tvFullName.setText(cursor.getString(cursor.getColumnIndexOrThrow("full_name")));
            tvEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            tvUsername.setText(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        }

        cursor.close();

        ivProfilePicture.setImageResource(R.drawable.default_profile_picture);


        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_profile);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_main) {
                    Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    return true;
                }
                return false;
            }
        });


        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Intent logoutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logoutIntent);
            finish();
        });
    }
}
