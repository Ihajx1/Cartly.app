package com.example.cartly2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;


public class RegistrationActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etConfirm;
    Button btnRegister, btnBack;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // initialize database
        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBackHome);
        RecyclerView recyclerTips = findViewById(R.id.recyclerTips);
        recyclerTips.setLayoutManager(new LinearLayoutManager(this));
        recyclerTips.setAdapter(new TipsAdapter(
                this,
                Arrays.asList(
                        "Use at least 6 characters",
                        "Include numbers/letters",
                        "Use a valid email"
                )
        ));


        btnRegister.setOnClickListener(v -> {
            if (validate()) {
                boolean inserted = dbHelper.registerUser(
                        etName.getText().toString().trim(),
                        etEmail.getText().toString().trim(),
                        etPassword.getText().toString().trim()
                );

                if (inserted) {
                    Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "User already exists or error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        // Link to login
        TextView tvLoginLink = findViewById(R.id.tvLoginLink);
        if (tvLoginLink != null) {
            tvLoginLink.setOnClickListener(v -> {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            });
        }
    }

    private boolean validate() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String confirm = etConfirm.getText().toString().trim();

        if (name.isEmpty()) { etName.setError("Required"); return false; }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email required"); return false;
        }
        if (pass.length() < 6) { etPassword.setError("At least 6 chars"); return false; }
        if (!pass.equals(confirm)) { etConfirm.setError("Passwords do not match"); return false; }
        return true;
    }
}

