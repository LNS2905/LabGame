package com.example.labgame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends Activity {

    private EditText etSignUpUsername;
    private EditText etSignUpPassword;
    private EditText etConfirmPassword;
    private Button btnSignUp;
    private TextView alreadyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        etSignUpUsername = findViewById(R.id.etSignUpUsername);
        etSignUpPassword = findViewById(R.id.etSignUpPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        alreadyAccount = findViewById(R.id.tvAlreadyAccount);

        btnSignUp.setOnClickListener(v -> {
            String username = etSignUpUsername.getText().toString();
            String password = etSignUpPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("UserCredentials", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Username", username);
                editor.putString("Password", password);
                editor.apply();

                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                // Quay lại LoginActivity sau khi đăng ký thành công
                finish();
            }
        });

        alreadyAccount.setOnClickListener(v -> finish
                ());
    }
}
