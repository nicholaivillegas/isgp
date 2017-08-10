package com.androidapp.isagip;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.isagip.model.Operation;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<Operation> operations;
    protected Context context;

    public RecyclerViewAdapter(Context context, List<Operation> operations) {
        this.operations = operations;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        viewHolder = new RecyclerViewHolder(layoutView, operations);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textName.setText(operations.get(position).getTitle());
        holder.textCalamity.setText(operations.get(position).getCreated_by());
        holder.textDate.setText(operations.get(position).getDate());
        holder.textDescription.setText(operations.get(position).getDescription());
        holder.textLocation.setText(operations.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return this.operations.size();
    }
}