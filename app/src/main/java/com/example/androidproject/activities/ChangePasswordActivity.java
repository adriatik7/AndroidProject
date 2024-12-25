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
import com.example.androidproject.repository.UserRepository;
import com.example.androidproject.utils.SessionManager;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText newPasswordEditText, confirmPasswordEditText;
    private Button changePasswordButton;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);


        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        userRepository = new UserRepository(this);


        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangePassword();
            }
        });
    }

    private void handleChangePassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New password and confirm password do not match.", Toast.LENGTH_SHORT).show();
            return;
        }


        int userId = sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Invalid session. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update password in database
        boolean isPasswordUpdated = userRepository.changePassword(userId, newPassword);

        if (isPasswordUpdated) {
            Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity or redirect to another screen
        } else {
            Toast.makeText(this, "Failed to update password. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
