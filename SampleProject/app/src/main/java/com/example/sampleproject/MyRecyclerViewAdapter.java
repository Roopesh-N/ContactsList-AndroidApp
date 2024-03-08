package com.example.sampleproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<contact> contactList;
    private ArrayList<contact> arraylist;
    private RecyclerViewClickListener Listener;

    public MyRecyclerViewAdapter(List<contact> contactList,RecyclerViewClickListener Listener) {
        this.contactList = contactList;
        this.Listener=Listener;
        this.arraylist = new ArrayList<contact>();
        this.arraylist.addAll(contactList);
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.setContactName(contactList.get(position).getContactName());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }

    //filter class
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        List<contact> filtered=new ArrayList<>();
        contactList.clear();
        if(charText.length()==0){
            contactList.addAll(arraylist);
        }else{
            for(contact wp:arraylist){
                if(wp.getContactName().toLowerCase(Locale.getDefault()).contains(charText)){
                    contactList.add(wp);
                }
            }
        }
        filtered=contactList;
        //check if filtered list is empty
        if(filtered.isEmpty()){
            contact noResults=new contact("No results found");
            contactList.add(noResults);
        }
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView name;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            name=itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);

        }

        public void setContactName(String contactName) {
            name.setText(contactName);
        }

        @Override
        public void onClick(View v) {
            Listener.onClick(v,getAdapterPosition());

        }
    }
}
