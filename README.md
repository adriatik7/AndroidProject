# SpendTracker Application

## Overview

- SpendTracker is an Android application built with Java that helps users track their daily expenses efficiently. It provides various categories for expense tracking, detailed statistics, and user management features.

## Key Features

 **User Registration**: 

- Users register by providing their full name, a valid email, and a password that must meet the following criteria:
     - Minimum 8 characters
     - At least one symbol
     - At least one number
- Once registered, an OTP is sent to the user's email for verification, ensuring a secure two-factor authentication (2FA) process.

 **2FA Implementation**:
 
- Email verification is handled using the JavaMail class.
- SMTP configuration:

 ```
 smtpHost = "smtp.gmail.com"  
 smtpPort = "587"  
 starttls = true 
 ```
 
- App password: Generated from the user's personal email to send verification emails securely.

 **Login & Password Reset**:

- Users can reset their passwords via email using the same 2FA process. Passwords are securely hashed using the SHA-256 algorithm.

 **SQLite Database**:

- The app uses SQLite to store data. Two main tables are utilized:
   - Users Table: Stores user information (e.g., full name, email, hashed password, etc.).
   - Items Table: Contains user-added expense items, connected by userID.
  
 **Expense Categories**:

- Users can add items across 8 categories: 
    - Food & Drinks
    - Transport
    - Entertainment
    - Bills & Utilities
    - Health
    - Shopping
    - Savings & Investment
    - Others 
- The app calculates the total expenses for each category, and these totals are displayed on the MainActivity.

 **MainActivity**:

- Displays the total expenses for each category.
- Users can add new expense items and view real-time updates on expenses.

 **StatisticsActivity**:

- Displays charts ranking categories by the amount spent.
- Shows the top 5 most expensive items, providing insights into spending patterns

 **ProfileActivity**:

- Displays user details and allows updating of username and password.
- A logout button is available, ensuring secure session management using SessionManager.


## Prerequisites

- Android Studio with Java development environment.
- SMTP configuration for email communication.
- SHA-256 algorithm for password hashing.

## How to Run

1. Clone the repository:
   ```
   https://github.com/adriatik7/AndroidProject
   ```
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.



     

