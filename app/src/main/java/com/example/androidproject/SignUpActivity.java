package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.database.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText fullNameField, emailField, usernameField, passwordField, confirmPasswordField;
    private Button signUpButton;

    private TextView footerText;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize fields
        fullNameField = findViewById(R.id.fullName);
        emailField = findViewById(R.id.email);
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.confirmPassword);
        signUpButton = findViewById(R.id.signUpButton);
        footerText = findViewById(R.id.footerText);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Set up sign-up button click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        footerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }




    private void handleSignUp() {
        String fullName = fullNameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String confirmPassword = confirmPasswordField.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save user in database
        boolean isInserted = dbHelper.addUser(fullName, email, username, password);

        if (isInserted) {
            Toast.makeText(this, "User added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add user.", Toast.LENGTH_SHORT).show();
        }


        if (isInserted) {
            Toast.makeText(this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
            // Navigate to LoginActivity
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Sign-Up Failed. Try again!", Toast.LENGTH_SHORT).show();
        }
    }
}
