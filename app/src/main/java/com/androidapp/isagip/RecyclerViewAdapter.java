package com.androidapp.isagip;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidapp.isagip.model.Operation;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<Operation> operations;
    protected Context context;
    View layoutView;

    public RecyclerViewAdapter(Context context, List<Operation> operations) {
        this.operations = operations;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder viewHolder = null;
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        viewHolder = new RecyclerViewHolder(layoutView, operations);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        holder.textName.setText(operations.get(position).getTitle());
        holder.textCalamity.setText(operations.get(position).getCreated_by());
        holder.textDate.setText(operations.get(position).getDate());
        holder.textDescription.setText(operations.get(position).getDescription());
        holder.textLocation.setText(operations.get(position).getLocation());
        holder.textReliefCount.setText(operations.get(position).getRelief_count());
        holder.textCreatedBy.setText(operations.get(position).getCreated_by());
        holder.cardNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.textCreatedBy.getVisibility() == View.VISIBLE) {
                    holder.textDescription.setVisibility(View.GONE);
                    holder.textReliefCount.setVisibility(View.GONE);
                    holder.textCreatedBy.setVisibility(View.GONE);
                }
                else {
                    holder.textDescription.setVisibility(View.VISIBLE);
                    holder.textReliefCount.setVisibility(View.VISIBLE);
                    holder.textCreatedBy.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.operations.size();
    }
}