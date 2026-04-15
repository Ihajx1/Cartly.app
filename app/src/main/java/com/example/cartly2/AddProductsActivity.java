package com.example.cartly2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AddProductsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText editTextItem;
    Button addButton, btnBack;
    DatabaseHelper dbHelper;
    ItemAdapter adapter;
    List<Item> itemList = new ArrayList<>();
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        // Get logged user ID from SharedPreferences
        userId = getSharedPreferences("CartlyPrefs", MODE_PRIVATE).getInt("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "Please log in first!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewItems);
        editTextItem = findViewById(R.id.editTextItem);
        addButton = findViewById(R.id.addButton);
        btnBack = findViewById(R.id.btnBackHome);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load items from the database
        itemList = dbHelper.getAllItems(userId);
        adapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(adapter);

        // Add new item
        addButton.setOnClickListener(v -> {
            String itemName = editTextItem.getText().toString().trim();
            if (!itemName.isEmpty()) {
                long newItemId = dbHelper.addItem(userId, itemName);
                if (newItemId != -1) {
                    // Refresh list to show correct item IDs
                    itemList.clear();
                    itemList.addAll(dbHelper.getAllItems(userId));
                    adapter.notifyDataSetChanged();
                    editTextItem.setText("");
                    Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error adding item", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter an item name", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle delete action on long click
        adapter.setOnItemLongClickListener(item -> {
            boolean deleted = dbHelper.deleteItem(item.getId());
            if (deleted) {
                // Refresh list after deletion
                itemList.clear();
                itemList.addAll(dbHelper.getAllItems(userId));
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button to main activity
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}