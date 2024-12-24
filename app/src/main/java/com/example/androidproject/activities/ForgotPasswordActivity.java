package com.example.androidproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.database.DatabaseHelper;
import com.example.androidproject.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailField;
    private Button resetButton;
    private ProgressBar progressBar;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailField = findViewById(R.id.emailField);
        resetButton = findViewById(R.id.resetButton);
        progressBar = findViewById(R.id.progressBar);

        dbHelper = new DatabaseHelper(this);

        resetButton.setOnClickListener(v -> handlePasswordReset());
    }

    private void handlePasswordReset() {
        String email = emailField.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        boolean isEmailExists = dbHelper.isEmailRegistered(email);

        progressBar.setVisibility(View.GONE);

        if (isEmailExists) {
            Intent intent = new Intent(ForgotPasswordActivity.this, VerifyForgotPasswordOTPActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
            Toast.makeText(this, "OTP code has been sent to youut email", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Email not registered", Toast.LENGTH_SHORT).show();
        }
    }
}
