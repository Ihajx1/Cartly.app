
package com.example.cartly2;

public class Item {
    private int id;
    private String itemName;

    private int userId;

    public Item(int id, String itemName,  int userId) {
        this.id = id;
        this.itemName = itemName;

        this.userId = userId;
    }

    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public int getUserId() { return userId; }
}
