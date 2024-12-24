package com.example.androidproject.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onEditItem(Item item);
        void onDeleteItem(Item item);
    }

    public ItemAdapter(List<Item> itemList, OnItemActionListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText("€" + item.getPrice());

        holder.itemView.setOnLongClickListener(v -> {
            // Pass the context and item to the dialog method
            showActionDialog(holder.itemView.getContext(), item);
            return true;
        });
    }

    private void showActionDialog(Context context, Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Action");
        builder.setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
            if (which == 0) {
                listener.onEditItem(item); // Edit Action
            } else if (which == 1) {
                listener.onDeleteItem(item); // Delete Action
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
        }
    }
}
