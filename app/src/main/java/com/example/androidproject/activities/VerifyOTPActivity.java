package com.example.androidproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.database.DatabaseHelper;
import com.example.androidproject.utils.OTPSender;
import com.example.androidproject.R;

import java.security.SecureRandom;

public class VerifyOTPActivity extends AppCompatActivity {
    private EditText otpEditText;
    private String generatedOtp, fullName, username, email, password;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otpEditText = findViewById(R.id.editTextOTP);
        Button verifyButton = findViewById(R.id.verifyButton);
        Button resendButton = findViewById(R.id.resendButton);
        dbHelper = new DatabaseHelper(this);

        // Get data from Intent
        fullName = getIntent().getStringExtra("fullName");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        generatedOtp = generateOtp();

        Log.d("VerifyOTPActivity", "Generated OTP: " + generatedOtp);

        // Send OTP email
        sendEmail(email, generatedOtp);

        verifyButton.setOnClickListener(v -> {
            String enteredOtp = otpEditText.getText().toString().trim();
            verifyOtp(enteredOtp);
        });

        resendButton.setOnClickListener(v -> {
            generatedOtp = generateOtp();
            Log.d("VerifyOTPActivity", "Resending OTP: " + generatedOtp);
            sendEmail(email, generatedOtp);
        });
    }

    private void verifyOtp(String enteredOtp) {
        if (TextUtils.isEmpty(enteredOtp)) {
            Toast.makeText(this, "Please enter the OTP.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (enteredOtp.equals(generatedOtp)) {
            // Insert user into the database
            if (dbHelper.addUser(fullName, email, username, password)) {
                Toast.makeText(this, "OTP verified successfully. Your account is now created.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error creating account. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = random.nextInt(900000) + 100000; // Generate a 6-digit OTP
        return String.valueOf(otp);
    }

    private void sendEmail(String email, String otp) {
        new Thread(() -> {
            boolean emailSent = OTPSender.sendEmail(email, otp);
            runOnUiThread(() -> {
                if (emailSent) {
                    Toast.makeText(this, "OTP sent to your email.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to send OTP. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
