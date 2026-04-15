package com.example.cartly2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Cartly.db";
    private static final int DB_VERSION = 2;

    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_NAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    private static final String TABLE_ITEMS = "items";
    private static final String COL_ITEM_ID = "id";
    private static final String COL_ITEM_NAME = "item_name";
    private static final String COL_USER_ID_FK = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ITEMS + " (" +
                COL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ITEM_NAME + " TEXT, " + " TEXT, " +
                COL_USER_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + COL_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    // Register new user
    public boolean registerUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, username);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Login existing user
    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE email=? AND password=?",
                new String[]{email, password});

        User user = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
            user = new User(id, name, email, password);
        }

        cursor.close();
        db.close();
        return user;
    }

    // Add item and return the new item ID
    public long addItem(int userId, String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ITEM_NAME, itemName);
        values.put(COL_USER_ID_FK, userId);
        long result = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return result; // returns new item ID
    }

    // Delete item by ID
    public boolean deleteItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ITEMS, "id=?", new String[]{String.valueOf(itemId)});
        db.close();
        return result > 0;
    }

    // Get all items for specific user
    public List<Item> getAllItems(int userId) {
        List<Item> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE user_id=?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ITEM_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_NAME));
                itemList.add(new Item(id, name, userId));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemList;
    }
}