package com.androidapp.isagip;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androidapp.isagip.model.Operation;

import java.util.List;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = RecyclerViewHolder.class.getSimpleName();
    public TextView textName, textCalamity, textLocation, textDescription, textDate;
    private List<Operation> operations;

    public RecyclerViewHolder(final View itemView, final List<Operation> operations) {
        super(itemView);
        this.operations = operations;
        textName = (TextView) itemView.findViewById(R.id.text_name);
        textCalamity = (TextView) itemView.findViewById(R.id.text_calamity);
        textLocation = (TextView) itemView.findViewById(R.id.text_location);
        textDescription = (TextView) itemView.findViewById(R.id.text_description);
        textDate = (TextView) itemView.findViewById(R.id.text_date);
    }
}
