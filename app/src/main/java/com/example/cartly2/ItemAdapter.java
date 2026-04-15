package com.example.cartly2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> itemList;
    private OnItemLongClickListener longClickListener;

    // Interface for handling long clicks on items
    public interface OnItemLongClickListener {
        void onItemLongClick(Item item);
    }

    // Setter for the long click listener
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    // ViewHolder class to hold item layout references
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.tvItemName.setText(item.getItemName());

        // Handle long click to trigger delete action
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(item);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
