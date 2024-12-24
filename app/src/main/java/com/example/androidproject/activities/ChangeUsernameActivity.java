package com.example.androidproject.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.example.androidproject.database.DatabaseHelper;
import com.example.androidproject.utils.SessionManager;

public class ChangeUsernameActivity extends AppCompatActivity {

    private EditText newUsernameEditText;
    private Button changeUsernameButton;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        // Initialize views
        newUsernameEditText = findViewById(R.id.newUsernameEditText);
        changeUsernameButton = findViewById(R.id.changeUsernameButton);

        // Initialize database helper and session manager
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Set change username button click listener
        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangeUsername();
            }
        });
    }

    private void handleChangeUsername() {
        String newUsername = newUsernameEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(newUsername)) {
            Toast.makeText(this, "New username is required.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get user ID from session
        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Invalid session. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update username in database
        boolean isUsernameUpdated = dbHelper.updateUsername(userId, newUsername);

        if (isUsernameUpdated) {
            Toast.makeText(this, "Username changed successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity or redirect to another screen
        } else {
            Toast.makeText(this, "Failed to update username. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
