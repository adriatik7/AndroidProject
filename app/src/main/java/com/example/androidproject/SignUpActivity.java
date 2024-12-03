package com.example.androidproject;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI elements
        TextInputEditText passwordField = findViewById(R.id.password);
        TextInputEditText confirmPasswordField = findViewById(R.id.confirmPassword);
        Button signUpButton = findViewById(R.id.signUpButton);
        TextView footerText = findViewById(R.id.footerText);

        // Handle Sign-Up Button click
        signUpButton.setOnClickListener(v -> {
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            // Validate password and confirm password
            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // Handle successful sign-up logic
                Toast.makeText(SignUpActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
            }
        });

        // Change color of "Login" text
        String fullText = "Already have an account? Login";
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("Login");
        int endIndex = startIndex + "Login".length();
        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#6200EE")), // Same color as Sign-Up button
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        footerText.setText(spannableString);

        // Add click listener for "Login"
        footerText.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}