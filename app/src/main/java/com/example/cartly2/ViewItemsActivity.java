package com.example.cartly2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.Button;


public class ViewItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private DatabaseHelper db;
    private int userId;
    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        // Check if user is logged in
        userId = getSharedPreferences("CartlyPrefs", MODE_PRIVATE).getInt("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "Please log in first!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);

        // Fetch only items that belong to the logged-in user
        List<Item> items = db.getAllItems(userId);

        if (items.isEmpty()) {
            Toast.makeText(this, "No items found for your account.", Toast.LENGTH_SHORT).show();
        }

        adapter = new ItemAdapter(items);
        recyclerView.setAdapter(adapter);

        btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener(v -> {
            startActivity(new Intent(ViewItemsActivity.this, MainActivity.class));
            finish();
        });
    }
}
