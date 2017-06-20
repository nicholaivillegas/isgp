package com.androidapp.isagip.adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidapp.isagip.R;

import java.util.ArrayList;

/**
 * Created by Jovi on 6/20/2017.
 */

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {
    private ArrayList<String> names = new ArrayList<>();
    private iMemberList listener;

    public interface iMemberList{
        void onItemChange();
    }

    public MemberListAdapter(iMemberList listener) {
        this.listener = listener;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
        listener.onItemChange();
    }

    public void addName(String name){
        names.add(name);
        notifyDataSetChanged();
        listener.onItemChange();
    }

    public void addNameOn(int index, String name){
        names.add(index, name);
        notifyItemInserted(index);
        listener.onItemChange();
    }

    public void removeName(int index){
        names.remove(index);
        notifyItemRemoved(index);
        listener.onItemChange();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_name, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textViewName.setText(names.get(position));
        holder.imgButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String backup = names.get(position);
                removeName(position);
                Snackbar.make(holder.itemView, backup + " successfully deleted.",Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addNameOn(position,backup);
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageButton imgButtonRemove;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textview_name);
            imgButtonRemove = (ImageButton) itemView.findViewById(R.id.imgbutton_remove);
        }
    }
}
