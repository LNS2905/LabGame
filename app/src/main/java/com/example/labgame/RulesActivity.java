package com.example.labgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class RulesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        Button btnAgree = findViewById(R.id.btnAgree);
        Button btnDisagree = findViewById(R.id.btnDisagree);

        btnAgree.setOnClickListener(v -> {
            // Chuyển đến MainActivity
            Intent intent = new Intent(RulesActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Đóng RulesActivity
        });

        btnDisagree.setOnClickListener(v -> {
            // Quay lại LoginActivity
            finish();
        });
    }
}

