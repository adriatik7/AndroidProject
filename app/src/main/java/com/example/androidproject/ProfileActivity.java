package com.example.androidproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private TextView tvFullName, tvEmail, tvUsername;
    private ImageView ivProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        tvFullName = findViewById(R.id.tvFullName);
        tvEmail = findViewById(R.id.tvEmail);
        tvUsername = findViewById(R.id.tvUsername);


        dbHelper = new DatabaseHelper(this);


        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        int userId = intent.getIntExtra("user_id", -1);



        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT full_name, email, username FROM users WHERE username = ?",
                new String[]{username}
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
                    mainIntent.putExtra("username", username);
                    intent.putExtra("user_id", userId);
                    startActivity(mainIntent);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    return true;
                }
                return false;
            }
        });
    }
}
