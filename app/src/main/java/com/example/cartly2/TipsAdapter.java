package com.example.cartly2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.VH> {
    private final Context ctx;
    private final List<String> data;

    public TipsAdapter(Context ctx, List<String> data){
        this.ctx = ctx; this.data = data;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_tip, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        String tip = data.get(position);
        h.txtTip.setText(tip);

        h.itemView.setOnClickListener(v ->
                Toast.makeText(ctx, "Tip: " + tip, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtTip;
        VH(@NonNull View itemView) {
            super(itemView);
            txtTip = itemView.findViewById(R.id.txtTip);
        }
    }
}