package com.example.cartly2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Welcome to Cartly!", Toast.LENGTH_SHORT).show();

        // Registration button
        Button btnGoRegister = findViewById(R.id.btnGoregistration);
        btnGoRegister.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class))
        );

        // Add Products button
        Button btnGoProducts = findViewById(R.id.btnGoProducts);
        btnGoProducts.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddProductsActivity.class))
        );

        // View My List button
        Button btnViewItems = findViewById(R.id.btnViewItems);
        btnViewItems.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ViewItemsActivity.class))
        );

        // Logout button
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            // Clear stored user session data
            getSharedPreferences("CartlyPrefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            // Redirect the user to the login screen
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

            // Show a logout confirmation message
            Toast.makeText(MainActivity.this, "You have logged out successfully.", Toast.LENGTH_SHORT).show();
        });

    }
}
