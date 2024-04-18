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

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.Viewholder>
{
    ArrayList<Barber> barbers;

    public BarberAdapter(ArrayList<Barber> barbers) {
        this.barbers = barbers;
    }

    @NonNull
    @Override
    public BarberAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlayout_barber_box, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberAdapter.Viewholder holder, int position) {
        holder.Name.setText(barbers.get(position).getName());
        holder.PhoneNumber.setText(barbers.get(position).getPhoneNumber());

        Glide.with(holder.itemView.getContext())
                .load(barbers.get(position).getPicAddress())
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return barbers.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder
    {
        TextView Name, PhoneNumber;
        com.google.android.material.imageview.ShapeableImageView pic;

        public Viewholder(@NonNull View itemView)
        {
            super(itemView);
            Name = itemView.findViewById((R.id.barber_name));
            PhoneNumber = itemView.findViewById(R.id.barbers_phone);
            pic = itemView.findViewById(R.id.barbers_pic);

        }
    }

    public Barber GetBarberByPosition(int position)
    {
        return barbers.get(position);
    }
}
