package com.example.androidproject.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.database.DatabaseHelper;
import com.example.androidproject.R;
import com.example.androidproject.repository.UserRepository;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText newPasswordEditText, confirmPasswordEditText;
    private Button resetPasswordButton;
    private DatabaseHelper dbHelper;
    private String email;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        dbHelper = new DatabaseHelper(this);
        userRepository = new UserRepository(this);

        email = getIntent().getStringExtra("email");

        resetPasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword)) {
                newPasswordEditText.setError("Password is required");
                return;
            }

            if (!isValidPassword(newPassword)) {
                Toast.makeText(this, "Password must be at least 8 characters long, contain at least one number and one special character.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords do not match");
                return;
            }

            if (userRepository.updatePassword(email, newPassword)) {
                Toast.makeText(this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update password. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidPassword(String newPassword) {
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        if (newPassword.length() < 8) {
            return false;
        }

        for (char c : newPassword.toCharArray()) {
            if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
            if (hasNumber && hasSpecialChar) {
                return true;
            }
        }
        return false;
    }
}
