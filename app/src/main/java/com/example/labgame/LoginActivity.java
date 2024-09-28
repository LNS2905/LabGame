package com.example.labgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        createAccount = findViewById(R.id.createAccount);

        loginButton.setOnClickListener(v -> {
            if (validateCredentials(username.getText().toString(), password.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                // Chuyển đến MainActivity sau khi đăng nhập thành công
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_LONG).show();
            }
        });

        createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    // Phương thức kiểm tra thông tin đăng nhập
    private boolean validateCredentials(String usernameInput, String passwordInput) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString("Username", "");
        String storedPassword = sharedPreferences.getString("Password", "");
        return usernameInput.equals(storedUsername) && passwordInput.equals(storedPassword);
    }
}
