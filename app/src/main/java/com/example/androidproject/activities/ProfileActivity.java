package com.example.androidproject.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.view.MenuInflater;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.database.DatabaseHelper;
import com.example.androidproject.R;
import com.example.androidproject.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private TextView tvFullName, tvEmail, tvUsername;
    private ImageView ivProfilePicture;
    private Button btnLogout;
    private ImageView ivSettings;

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
        ivSettings = findViewById(R.id.ivSettings);

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


        ivSettings.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(ProfileActivity.this, v);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_settings, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_change_username) {
                    Intent usernameIntent = new Intent(ProfileActivity.this, ChangeUsernameActivity.class);
                    startActivity(usernameIntent);
                    return true;
                } else if (item.getItemId() == R.id.menu_change_password) {
                    Intent passwordIntent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                    startActivity(passwordIntent);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });

        ivProfilePicture.setImageResource(R.drawable.default_profile_picture);


        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_main) {
                Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            } else if (itemId == R.id.nav_stats) {
                Intent statsIntent = new Intent(ProfileActivity.this, StatisticsActivity.class);
                startActivity(statsIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
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
