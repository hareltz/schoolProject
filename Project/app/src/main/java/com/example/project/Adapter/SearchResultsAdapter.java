package com.example.project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Domain.Barber;
import com.example.project.R;

import java.util.ArrayList;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.Viewholder>
{
    ArrayList<Barber> searchResult;

    public SearchResultsAdapter(ArrayList<Barber> searchResult) {
        this.searchResult = searchResult;
    }

    @NonNull
    @Override
    public SearchResultsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlayout_search_result, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsAdapter.Viewholder holder, int position) {
        holder.Name.setText(searchResult.get(position).getName());
        holder.PhoneNumber.setText(searchResult.get(position).getPhoneNumber());
        holder.Address.setText(searchResult.get(position).getAddress());
        holder.Price.setText(searchResult.get(position).getPrice());

        Glide.with(holder.itemView.getContext())
                .load(searchResult.get(position).getPicAddress())
                .into(holder.Pic);
    }

    @Override
    public int getItemCount() {
        return searchResult.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder
    {
        TextView Name, PhoneNumber, Address, Price;
        com.google.android.material.imageview.ShapeableImageView Pic;

        public Viewholder(@NonNull View itemView)
        {
            super(itemView);
            Name = itemView.findViewById((R.id.search_results_barbers_name));
            PhoneNumber = itemView.findViewById(R.id.search_results_barbers_phone);
            Pic = itemView.findViewById(R.id.search_results_barber_pic);
            Address = itemView.findViewById(R.id.search_results_address);
            Price = itemView.findViewById(R.id.search_results_price);
        }
    }
}
